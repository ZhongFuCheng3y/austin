package com.java3y.austin.support.mq.springeventbus;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * 描述：消息
 *
 * @author tony
 * @date 2023/2/6 19:59
 */
@Data
public class SpringEventBusEvent extends ApplicationEvent {
    public String topic;
    public String jsonValue;
    public String tagId;

    public SpringEventBusEvent(Object source, String topic, String jsonValue, String tagId) {
        super(source);
        this.topic = topic;
        this.jsonValue = jsonValue;
        this.tagId = tagId;
    }

    public String getTopic() {
        return topic;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public String getTagId() {
        return tagId;
    }
}
