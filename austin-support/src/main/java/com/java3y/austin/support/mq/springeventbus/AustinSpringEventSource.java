package com.java3y.austin.support.mq.springeventbus;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 3y
 */
@Data
@Builder
public class AustinSpringEventSource implements Serializable {
    private String topic;
    private String jsonValue;
    private String tagId;
}
