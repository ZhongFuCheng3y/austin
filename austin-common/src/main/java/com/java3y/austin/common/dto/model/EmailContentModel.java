package com.java3y.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y
 * <p>
 * <p>
 * 邮件消息体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailContentModel extends ContentModel {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容(可写入HTML)
     */
    private String content;


}
