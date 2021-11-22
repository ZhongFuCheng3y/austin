package com.java3y.austin.action;

import com.java3y.austin.domain.SendTaskModel;
import com.java3y.austin.pipeline.BusinessProcess;
import com.java3y.austin.pipeline.ProcessContext;

/**
 * @author 3y
 * @date 2021/11/22
 * @description 参数校验
 */
public class ParamAction implements BusinessProcess {
    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
    }
}
