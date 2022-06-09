package com.java3y.austin.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.service.api.enums.BusinessCode;
import com.java3y.austin.service.api.impl.domain.SendTaskModel;
import com.java3y.austin.support.pipeline.BusinessProcess;
import com.java3y.austin.support.pipeline.ProcessContext;
import com.java3y.austin.support.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author 3y
 * 将消息发送到MQ
 */
@Slf4j
@Service
public class SendMqAction implements BusinessProcess<SendTaskModel> {

    @Autowired
    private KafkaUtils kafkaUtils;

    @Value("${austin.business.topic.name}")
    private String sendMessageTopic;

    @Value("${austin.business.recall.topic.name}")
    private String austinRecall;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        try {
            if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())) {
                String message = JSON.toJSONString(sendTaskModel.getTaskInfo(), new SerializerFeature[]{SerializerFeature.WriteClassName});
                kafkaUtils.send(sendMessageTopic, message);
            } else if (BusinessCode.RECALL.getCode().equals(context.getCode())) {
                String message = JSON.toJSONString(sendTaskModel.getMessageTemplate(), new SerializerFeature[]{SerializerFeature.WriteClassName});
                kafkaUtils.send(austinRecall, message);
            }
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR));
            log.error("send kafka fail! e:{},params:{}", Throwables.getStackTraceAsString(e)
                    , JSON.toJSONString(CollUtil.getFirst(sendTaskModel.getTaskInfo().listIterator())));
        }
    }
}
