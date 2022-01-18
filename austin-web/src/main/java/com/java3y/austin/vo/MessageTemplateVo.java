package com.java3y.austin.vo;

import com.java3y.austin.domain.MessageTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 消息模板的Vo
 *
 * @author 3y
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateVo {

    /**
     * 消息模板李彪
     */
    private Iterable<MessageTemplate> rows;

    /**
     * 总条数
     */
    private Long count;
}
