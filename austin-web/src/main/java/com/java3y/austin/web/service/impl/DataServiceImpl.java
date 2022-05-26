package com.java3y.austin.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.common.domain.SimpleAnchorInfo;
import com.java3y.austin.common.enums.AnchorState;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.common.enums.SmsStatus;
import com.java3y.austin.support.dao.MessageTemplateDao;
import com.java3y.austin.support.dao.SmsRecordDao;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.support.domain.SmsRecord;
import com.java3y.austin.support.utils.RedisUtils;
import com.java3y.austin.support.utils.TaskInfoUtils;
import com.java3y.austin.web.constants.AmisVoConstant;
import com.java3y.austin.web.service.DataService;
import com.java3y.austin.web.vo.DataParam;
import com.java3y.austin.web.vo.amis.EchartsVo;
import com.java3y.austin.web.vo.amis.SmsTimeLineVo;
import com.java3y.austin.web.vo.amis.UserTimeLineVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据链路追踪获取接口 实现类
 *
 * @author 3y
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Autowired
    private SmsRecordDao smsRecordDao;


    @Override
    public UserTimeLineVo getTraceUserInfo(String receiver) {
        List<String> userInfoList = redisUtils.lRange(receiver, 0, -1);
        if (CollUtil.isEmpty(userInfoList)) {
            return UserTimeLineVo.builder().items(new ArrayList<>()).build();
        }

        // 0. 按时间排序
        List<SimpleAnchorInfo> sortAnchorList = userInfoList.stream().map(s -> JSON.parseObject(s, SimpleAnchorInfo.class)).sorted((o1, o2) -> Math.toIntExact(o1.getTimestamp() - o2.getTimestamp())).collect(Collectors.toList());

        // 1. 对相同的businessId进行分类  {"businessId":[{businessId,state,timeStamp},{businessId,state,timeStamp}]}
        Map<String, List<SimpleAnchorInfo>> map = MapUtil.newHashMap();
        for (SimpleAnchorInfo simpleAnchorInfo : sortAnchorList) {
            List<SimpleAnchorInfo> simpleAnchorInfos = map.get(String.valueOf(simpleAnchorInfo.getBusinessId()));
            if (CollUtil.isEmpty(simpleAnchorInfos)) {
                simpleAnchorInfos = new ArrayList<>();
            }
            simpleAnchorInfos.add(simpleAnchorInfo);
            map.put(String.valueOf(simpleAnchorInfo.getBusinessId()), simpleAnchorInfos);
        }

        // 2. 封装vo 给到前端渲染展示
        List<UserTimeLineVo.ItemsVO> items = new ArrayList<>();
        for (Map.Entry<String, List<SimpleAnchorInfo>> entry : map.entrySet()) {
            Long messageTemplateId = TaskInfoUtils.getMessageTemplateIdFromBusinessId(Long.valueOf(entry.getKey()));
            MessageTemplate messageTemplate = messageTemplateDao.findById(messageTemplateId).get();

            StringBuilder sb = new StringBuilder();
            for (SimpleAnchorInfo simpleAnchorInfo : entry.getValue()) {
                if (AnchorState.RECEIVE.getCode().equals(simpleAnchorInfo.getState())) {
                    sb.append(StrPool.CRLF);
                }
                String startTime = DateUtil.format(new Date(simpleAnchorInfo.getTimestamp()), DatePattern.NORM_DATETIME_PATTERN);
                String stateDescription = AnchorState.getDescriptionByCode(simpleAnchorInfo.getState());
                sb.append(startTime).append(StrPool.C_COLON).append(stateDescription).append("==>");
            }

            for (String detail : sb.toString().split(StrPool.CRLF)) {
                if (StrUtil.isNotBlank(detail)) {
                    UserTimeLineVo.ItemsVO itemsVO = UserTimeLineVo.ItemsVO.builder()
                            .businessId(entry.getKey())
                            .sendType(ChannelType.getEnumByCode(messageTemplate.getSendChannel()).getDescription())
                            .creator(messageTemplate.getCreator())
                            .title(messageTemplate.getName())
                            .detail(detail)
                            .build();
                    items.add(itemsVO);
                }
            }
        }
        return UserTimeLineVo.builder().items(items).build();
    }

    @Override
    public EchartsVo getTraceMessageTemplateInfo(String businessId) {

        // 1. 获取businessId并获取模板信息
        businessId = getRealBusinessId(businessId);
        Optional<MessageTemplate> optional = messageTemplateDao.findById(TaskInfoUtils.getMessageTemplateIdFromBusinessId(Long.valueOf(businessId)));
        if (!optional.isPresent()) {
            return null;
        }
        MessageTemplate messageTemplate = optional.get();

        List<String> xAxisList = new ArrayList<>();
        List<Integer> actualData = new ArrayList<>();

        /**
         * key：state
         * value:stateCount
         */
        Map<Object, Object> anchorResult = redisUtils.hGetAll(getRealBusinessId(businessId));
        if (CollUtil.isNotEmpty(anchorResult)) {
            anchorResult = MapUtil.sort(anchorResult);
            for (Map.Entry<Object, Object> entry : anchorResult.entrySet()) {
                String description = AnchorState.getDescriptionByCode(Integer.valueOf(String.valueOf(entry.getKey())));
                xAxisList.add(description);
                actualData.add(Integer.valueOf(String.valueOf(entry.getValue())));
            }
        }


        String title = "【" + messageTemplate.getName() + "】在" + TaskInfoUtils.getDateFromBusinessId(Long.valueOf(businessId)) + "的下发情况：";

        return EchartsVo.builder()
                .title(EchartsVo.TitleVO.builder().text(title).build())
                .legend(EchartsVo.LegendVO.builder().data(Arrays.asList(AmisVoConstant.LEGEND_TITLE)).build())
                .xAxis(EchartsVo.XAxisVO.builder().data(xAxisList).build())
                .series(Arrays.asList(EchartsVo.SeriesVO.builder().name(AmisVoConstant.LEGEND_TITLE).type(AmisVoConstant.TYPE).data(actualData).build()))
                .yAxis(EchartsVo.YAxisVO.builder().build())
                .tooltip(EchartsVo.TooltipVO.builder().build())
                .build();

    }

    @Override
    public SmsTimeLineVo getTraceSmsInfo(DataParam dataParam) {

        ArrayList<SmsTimeLineVo.ItemsVO> itemsVOS = new ArrayList<>();
        SmsTimeLineVo smsTimeLineVo = SmsTimeLineVo.builder().items(itemsVOS).build();

        Integer sendDate = Integer.valueOf(DateUtil.format(new Date(dataParam.getDateTime() * 1000L)
                , DatePattern.PURE_DATE_PATTERN));
        List<SmsRecord> smsRecordList = smsRecordDao.findByPhoneAndSendDate(Long.valueOf(dataParam.getReceiver()), sendDate);

        if (CollUtil.isEmpty(smsRecordList)) {
            return smsTimeLineVo;
        }

        Map<String, List<SmsRecord>> maps = smsRecordList.stream().collect(Collectors.groupingBy((o) -> o.getPhone() + o.getSeriesId()));

        for (Map.Entry<String, List<SmsRecord>> entry : maps.entrySet()) {
            SmsTimeLineVo.ItemsVO itemsVO = SmsTimeLineVo.ItemsVO.builder().build();
            for (SmsRecord smsRecord : entry.getValue()) {
                // 发送记录 messageTemplateId >0 ,回执记录 messageTemplateId =0
                if (smsRecord.getMessageTemplateId() > 0) {
                    itemsVO.setBusinessId(String.valueOf(smsRecord.getMessageTemplateId()));
                    itemsVO.setContent(smsRecord.getMsgContent());
                    itemsVO.setSendType(SmsStatus.getDescriptionByStatus(smsRecord.getStatus()));
                    itemsVO.setSendTime(DateUtil.format(new Date(Long.valueOf(smsRecord.getCreated()*1000L)), DatePattern.NORM_DATETIME_PATTERN));

                } else {
                    itemsVO.setReceiveType(SmsStatus.getDescriptionByStatus(smsRecord.getStatus()));
                    itemsVO.setReceiveContent(smsRecord.getReportContent());
                    itemsVO.setReceiveTime(DateUtil.format(new Date(Long.valueOf(smsRecord.getUpdated()*1000L)), DatePattern.NORM_DATETIME_PATTERN));
                }
            }
            itemsVOS.add(itemsVO);
        }

        return smsTimeLineVo;
    }

    /**
     * 如果传入的是模板ID，则生成【当天】的businessId进行查询
     * 如果传入的是businessId，则按默认的businessId进行查询
     * 判断是否为businessId则判断长度是否为16位（businessId长度固定16)
     */
    private String getRealBusinessId(String businessId) {
        if (AustinConstant.BUSINESS_ID_LENGTH == businessId.length()) {
            return businessId;
        }
        Optional<MessageTemplate> optional = messageTemplateDao.findById(Long.valueOf(businessId));
        if (optional.isPresent()) {
            MessageTemplate messageTemplate = optional.get();
            return String.valueOf(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()));
        }
        return businessId;
    }
}
