package com.java3y.austin.common.pipeline;

import com.java3y.austin.common.vo.BasicResultVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 责任链上下文
 *
 * @author 3y
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ProcessContext<T extends ProcessModel> implements Serializable {
    /**
     * 标识责任链的code
     */
    private String code;
    /**
     * 存储责任链上下文数据的模型
     */
    private T processModel;
    /**
     * 责任链中断的标识
     */
    private Boolean needBreak;
    /**
     * 流程处理的结果
     */
    private BasicResultVO response;

}
