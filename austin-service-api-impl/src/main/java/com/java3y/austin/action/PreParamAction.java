package com.java3y.austin.action;

import cn.hutool.core.collection.CollUtil;
import com.java3y.austin.enums.RespStatusEnum;
import com.java3y.austin.domain.MessageParam;
import com.java3y.austin.domain.SendTaskModel;
import com.java3y.austin.pipeline.BusinessProcess;
import com.java3y.austin.pipeline.ProcessContext;
import com.java3y.austin.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 3y
 * @date 2021/11/22
 * @description 前置参数校验
 */
@Slf4j
public class PreParamAction implements BusinessProcess {

    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();

        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();

        if (messageTemplateId == null || CollUtil.isEmpty(messageParamList)) {
            context.setNeedBreak(true);
            context.setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
        }
    }
}
