package com.java3y.austin.cron.pending;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrPool;
import com.google.common.collect.Lists;
import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.cron.config.CronAsyncThreadPoolConfig;
import com.java3y.austin.cron.constants.PendingConstant;
import com.java3y.austin.cron.vo.CrowdInfoVo;
import com.java3y.austin.service.api.domain.BatchSendRequest;
import com.java3y.austin.service.api.domain.MessageParam;
import com.java3y.austin.service.api.enums.BusinessCode;
import com.java3y.austin.service.api.service.SendService;
import com.java3y.austin.support.pending.AbstractLazyPending;
import com.java3y.austin.support.pending.PendingParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 延迟批量处理人群信息
 * 调用 batch 发送接口 进行消息推送
 *
 * @author 3y
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CrowdBatchTaskPending extends AbstractLazyPending<CrowdInfoVo> {

    @Autowired
    private SendService sendService;

    public CrowdBatchTaskPending() {
        PendingParam<CrowdInfoVo> pendingParam = new PendingParam<>();
        pendingParam.setQueue(new LinkedBlockingQueue(PendingConstant.QUEUE_SIZE))
                .setTimeThreshold(PendingConstant.TIME_THRESHOLD)
                .setNumThreshold(AustinConstant.BATCH_RECEIVER_SIZE)
                .setExecutorService(CronAsyncThreadPoolConfig.getConsumePendingThreadPool());
        this.pendingParam = pendingParam;
    }

    @Override
    public void doHandle(List<CrowdInfoVo> crowdInfoVos) {

        // 1. 如果参数相同，组装成同一个MessageParam发送
        Map<Map<String, String>, String> paramMap = MapUtil.newHashMap();
        for (CrowdInfoVo crowdInfoVo : crowdInfoVos) {
            String receiver = crowdInfoVo.getReceiver();
            Map<String, String> vars = crowdInfoVo.getParams();
            if (Objects.isNull(paramMap.get(vars))) {
                paramMap.put(vars, receiver);
            } else {
                String newReceiver = StringUtils.join(new String[]{
                        paramMap.get(vars), receiver}, StrPool.COMMA);
                paramMap.put(vars, newReceiver);
            }
        }

        // 2. 组装参数
        List<MessageParam> messageParams = Lists.newArrayList();
        for (Map.Entry<Map<String, String>, String> entry : paramMap.entrySet()) {
            MessageParam messageParam = MessageParam.builder().receiver(entry.getValue())
                    .variables(entry.getKey()).build();
            messageParams.add(messageParam);
        }

        // 3. 调用批量发送接口发送消息
        BatchSendRequest batchSendRequest = BatchSendRequest.builder().code(BusinessCode.COMMON_SEND.getCode())
                .messageParamList(messageParams)
                .messageTemplateId(CollUtil.getFirst(crowdInfoVos.iterator()).getMessageTemplateId())
                .build();
        sendService.batchSend(batchSendRequest);
    }

}
