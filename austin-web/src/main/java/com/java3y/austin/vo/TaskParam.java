package com.java3y.austin.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * xxlJob任务的参数
 * @author 3y
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskParam {

    /**
     * 模板Id
     */
    private String messageTemplateId;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 创建者
     */
    private String creator;


    /**
     * 额外参数信息
     */
    private Map<String,Object> extra;

}
