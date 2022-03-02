package com.java3y.austin.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简单的埋点信息
 *
 * @author 3y
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleAnchorInfo {


    /**
     * 具体点位
     */
    private int state;

    /**
     * 业务Id(数据追踪使用)
     * 生成逻辑参考 TaskInfoUtils
     */
    private Long businessId;

    /**
     * 生成时间
     */
    private long timestamp;
}
