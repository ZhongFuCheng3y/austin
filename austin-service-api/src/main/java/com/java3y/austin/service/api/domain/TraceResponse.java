package com.java3y.austin.service.api.domain;

import com.java3y.austin.common.domain.SimpleAnchorInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: sky
 * @Date: 2023/7/13 13:38
 * @Description: TraceResponse
 * @Version 1.0.0
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TraceResponse {
    /**
     * 响应状态
     */
    private String code;
    /**
     * 响应编码
     */
    private String msg;

    /**
     * 埋点信息
     */
    private List<SimpleAnchorInfo> data;
}
