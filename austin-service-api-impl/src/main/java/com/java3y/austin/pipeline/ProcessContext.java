package com.java3y.austin.pipeline;

/**
 * 责任链上下文
 * @author 3y
 */
public class ProcessContext {

    /**
     * 标识责任链的code
     */
    private String code;

    /**
     * 存储责任链上下文数据的模型
     */
    private ProcessModel processModel;

    /**
     * 责任链中断的标识
     */
    private Boolean needBreak = false;

}
