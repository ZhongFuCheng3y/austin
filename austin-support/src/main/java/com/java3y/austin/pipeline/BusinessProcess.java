package com.java3y.austin.pipeline;

/**
 * 业务执行器
 *
 * @author 3y
 */
public interface BusinessProcess {

    /**
     * 真正处理逻辑
     * @param context
     */
    void process(ProcessContext context);
}
