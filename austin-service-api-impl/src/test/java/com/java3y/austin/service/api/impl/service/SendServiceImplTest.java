package com.java3y.austin.service.api.impl.service;

import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.service.api.domain.BatchSendRequest;
import com.java3y.austin.service.api.domain.MessageParam;
import com.java3y.austin.service.api.domain.SendRequest;
import com.java3y.austin.service.api.domain.SendResponse;
import com.java3y.austin.service.api.enums.BusinessCode;
import com.java3y.austin.service.api.impl.domain.SendTaskModel;
import com.java3y.austin.support.pipeline.BusinessProcess;
import com.java3y.austin.support.pipeline.ProcessContext;
import com.java3y.austin.support.pipeline.ProcessController;
import com.java3y.austin.support.pipeline.ProcessTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendServiceImplTest {

    @Spy
    private ProcessController processController;

    @Mock
    private Map<String, ProcessTemplate> templateConfig;

    @Spy
    private ProcessTemplate processTemplate;

    @Mock
    private BusinessProcess businessProcess;

    @InjectMocks
    private SendServiceImpl sendServiceImplUnderTest;

    @Test
    void testSend() {

//        // params
//        final SendRequest sendRequest = new SendRequest("send", 1L,
//                new MessageParam("13711111111", new HashMap<>(), new HashMap<>()));
//
//        // predict result
//        final ProcessContext<SendTaskModel> processContext = new ProcessContext<>(sendRequest.getCode(), new SendTaskModel(), false, new BasicResultVO<>(
//                RespStatusEnum.SUCCESS, "data"));
//        final SendResponse expectedResult = new SendResponse(processContext.getResponse().getStatus(), processContext.getResponse().getMsg());
//
//
//        // stub
//        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
//        processTemplate.setProcessList(Arrays.asList(businessProcess));
//        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), processTemplate);
//
//        processController.setTemplateConfig(templateConfig);
//
//
//        // Run the test
//        final SendResponse result = sendServiceImplUnderTest.send(sendRequest);
//
//        // Verify the results
//        assertEquals(expectedResult, result);
    }

    @Test
    void testBatchSend() {
//        // Setup
//        final BatchSendRequest batchSendRequest = new BatchSendRequest("code", 0L,
//                Arrays.asList(new MessageParam("receiver", new HashMap<>(), new HashMap<>())));
//        final SendResponse expectedResult = new SendResponse("status", "msg");
//
//        // Configure ProcessController.process(...).
//        final ProcessContext processContext = new ProcessContext<>("code", null, false, new BasicResultVO<>(
//                RespStatusEnum.SUCCESS, "data"));
//        when(processController.process(new ProcessContext<>("code", null, false, new BasicResultVO<>(
//                RespStatusEnum.SUCCESS, "data")))).thenReturn(processContext);
//
//        // Run the test
//        final SendResponse result = sendServiceImplUnderTest.batchSend(batchSendRequest);
//
//        // Verify the results
//        assertEquals(expectedResult, result);
    }
}
