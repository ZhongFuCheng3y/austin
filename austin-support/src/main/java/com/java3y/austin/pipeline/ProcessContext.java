package com.java3y.austin.pipeline;

import com.java3y.austin.vo.BasicResultVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 责任链上下文
 * @author 3y
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    /**
     * 流程处理的结果
     */
    BasicResultVO response = BasicResultVO.success();

}
