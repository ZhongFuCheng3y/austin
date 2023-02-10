package com.java3y.austin.support.mq.springeventbus;

import lombok.Builder;
import lombok.Data;

/**
 * @author 3y
 */
@Data
@Builder
public class AustinSpringEventSource {
    public String topic;
    public String jsonValue;
    public String tagId;
}
