package com.java3y.austin.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.dao.SmsRecordDao;
import com.java3y.austin.domain.AnchorInfo;
import com.java3y.austin.domain.SmsParam;
import com.java3y.austin.domain.SmsRecord;
import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.dto.SmsContentModel;
import com.java3y.austin.enums.AnchorState;
import com.java3y.austin.enums.ChannelType;
import com.java3y.austin.script.SmsScript;
import com.java3y.austin.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 短信发送处理
 *
 * @author 3y
 */
@Component
@Slf4j
public class SmsHandler extends Handler {

    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }

    @Autowired
    private SmsRecordDao smsRecordDao;

    @Autowired
    private SmsScript smsScript;


    @Override
    public boolean handler(TaskInfo taskInfo) {
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .supplierId(10)
                .supplierName("腾讯云通知类消息渠道").build();
        try {
            List<SmsRecord> recordList = smsScript.send(smsParam);
            if (!CollUtil.isEmpty(recordList)) {
                smsRecordDao.saveAll(recordList);
            }
            return true;
        } catch (Exception e) {
            log.error("SmsHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
        }
        return false;
    }

    /**
     * 如果有输入链接，则把链接拼在文案后
     * <p>
     * PS: 这里可以考虑将链接 转 短链
     * PS: 如果是营销类的短信，需考虑拼接 回TD退订 之类的文案
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            return smsContentModel.getContent() + " " + smsContentModel.getUrl();
        } else {
            return smsContentModel.getContent();
        }
    }


}
