package com.java3y.austin.service.api.impl.config;


import com.java3y.austin.service.api.enums.BusinessCode;
import com.java3y.austin.service.api.impl.action.AfterParamCheckAction;
import com.java3y.austin.service.api.impl.action.AssembleAction;
import com.java3y.austin.service.api.impl.action.PreParamCheckAction;
import com.java3y.austin.service.api.impl.action.SendMqAction;
import com.java3y.austin.support.pipeline.BusinessProcess;
import com.java3y.austin.support.pipeline.ProcessController;
import com.java3y.austin.support.pipeline.ProcessTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * api层的pipeline配置类
 * @author 3y
 */
@Configuration
public class PipelineConfig {


    /**
     * 普通发送执行流程
     * 1. 前置参数校验
     * 2. 组装参数
     * 3. 后置参数校验
     * 4. 发送消息至MQ
     * @return
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        ArrayList<BusinessProcess> processList = new ArrayList<>();

        processList.add(preParamCheckAction());
        processList.add(assembleAction());
        processList.add(afterParamCheckAction());
        processList.add(sendMqAction());

        processTemplate.setProcessList(processList);
        return processTemplate;
    }

    /**
     * pipeline流程控制器
     * 目前暂定只有 普通发送的流程
     * 后续扩展则加BusinessCode和ProcessTemplate
     *
     * @return
     */
    @Bean
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), commonSendTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }


    /**
     * 组装参数Action
     *
     * @return
     */
    @Bean
    public AssembleAction assembleAction() {
        return new AssembleAction();
    }

    /**
     * 前置参数校验Action
     *
     * @return
     */
    @Bean
    public PreParamCheckAction preParamCheckAction() {
        return new PreParamCheckAction();
    }

    /**
     * 后置参数校验Action
     *
     * @return
     */
    @Bean
    public AfterParamCheckAction afterParamCheckAction() {
        return new AfterParamCheckAction();
    }

    /**
     * 发送消息至MQ的Action
     *
     * @return
     */
    @Bean
    public SendMqAction sendMqAction() {
        return new SendMqAction();
    }

}
