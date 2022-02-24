package com.java3y.austin.web.service.impl;

import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.common.enums.AnchorState;
import com.java3y.austin.support.dao.MessageTemplateDao;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.support.utils.RedisUtils;
import com.java3y.austin.support.utils.TaskInfoUtils;
import com.java3y.austin.web.service.DataService;
import com.java3y.austin.web.vo.amis.EchartsVo;
import com.java3y.austin.web.vo.amis.TimeLineItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    @Override
    public TimeLineItemVo getTraceUserInfo(String receiver) {
        return null;
    }

    @Override
    public EchartsVo getTraceMessageTemplateInfo(String businessId) {
        /**
         * key：state
         * value:stateCount
         */
        Map<Object, Object> anchorResult = redisUtils.hGetAll(getRealBusinessId(businessId));
        List<Integer> stateList = anchorResult.entrySet().stream().map(objectObjectEntry -> Integer.valueOf(String.valueOf(objectObjectEntry.getKey()))).collect(Collectors.toList());
        for (AnchorState value : AnchorState.values()) {

        }

        return null;
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
        MessageTemplate messageTemplate = messageTemplateDao.findById(Long.valueOf(businessId)).get();
        if (messageTemplate != null) {
            return String.valueOf(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()));
        }
        return businessId;
    }

}
