package com.java3y.austin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息模板管理 请求参数
 * @author 3y
 * @date 2022/1/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateParam {

    /**
     * 当前页码
     */
    private Integer page ;

    /**
     * 当前页大小
     */
    private Integer perPage;


}
