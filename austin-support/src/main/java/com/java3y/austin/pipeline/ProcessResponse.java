package com.java3y.austin.pipeline;

import lombok.Builder;

/**
 * @author 3y
 * @date 2021/11/22
 * @cription 流程处理的结果
 */
@Builder
public class ProcessResponse {

    /** 返回值编码 */
    private final String code;

    /** 返回值描述 */
    private final String description;

}
