package com.java3y.austin.service;

import com.java3y.austin.domain.BatchSendRequest;
import com.java3y.austin.domain.SendRequest;
import com.java3y.austin.domain.SendResponse;
import com.java3y.austin.domain.SendTaskModel;
import com.java3y.austin.enums.RequestType;
import com.java3y.austin.pipeline.ProcessContext;
import com.java3y.austin.pipeline.ProcessController;
import com.java3y.austin.pojo.TaskInfo;
import com.java3y.austin.vo.BasicResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发送接口
 * @author 3y
 */
@Service
public class SendServiceImpl implements SendService  {

    @Autowired
    private ProcessController processController;

    @Override
    public SendResponse send(SendRequest sendRequest) {

        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .requestType(RequestType.SINGLE.getCode())
                .messageParam(sendRequest.getMessageParam())
                .taskInfo(TaskInfo.builder().messageTemplateId(sendRequest.getMessageTemplateId()).build())
                .build();

        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel).build();

        ProcessContext process = processController.process(context);

        return new SendResponse(process.getResponse().getCode(), process.getResponse().getMsg());
    }

    @Override
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .requestType(RequestType.BATCH.getCode())
                .messageParamList(batchSendRequest.getMessageParamList())
                .taskInfo(TaskInfo.builder().messageTemplateId(batchSendRequest.getMessageTemplateId()).build())
                .build();

        ProcessContext context = ProcessContext.builder()
                .code(batchSendRequest.getCode())
                .processModel(sendTaskModel).build();

        ProcessContext process = processController.process(context);

        return new SendResponse(process.getResponse().getCode(), process.getResponse().getMsg());
    }


}
