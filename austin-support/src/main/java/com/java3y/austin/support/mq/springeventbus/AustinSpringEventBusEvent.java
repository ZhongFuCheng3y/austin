package com.java3y.austin.support.mq.springeventbus;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 描述：消息
 *
 * @author tony
 * @date 2023/2/6 19:59
 */
@Getter
public class AustinSpringEventBusEvent extends ApplicationEvent {

    private final AustinSpringEventSource austinSpringEventSource;

    public AustinSpringEventBusEvent(Object source, AustinSpringEventSource austinSpringEventSource) {
        super(source);
        this.austinSpringEventSource = austinSpringEventSource;
    }

}
