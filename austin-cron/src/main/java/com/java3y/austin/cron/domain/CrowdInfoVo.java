package com.java3y.austin.cron.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * 每一行csv的记录
 * @author 3y
 * @date 2022/2/9
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrowdInfoVo implements Serializable {

    /**
     * 接收者id
     */
    private String id;

    /**
     * 参数信息
     */
    private Map<String, String> params;
}
