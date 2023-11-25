package com.java3y.austin.handler.handler;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * channel->Handler的映射关系
 *
 * @author 3y
 */
@Component
public class HandlerHolder {

    private Map<Integer, Handler> handlers = new HashMap<>(128);

    public void putHandler(Integer channelCode, Handler handler) {
        handlers.put(channelCode, handler);
    }

    public Handler route(Integer channelCode) {
        return handlers.get(channelCode);
    }
}
