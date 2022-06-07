package com.java3y.austin.handler.script;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * sendAccount->SmsScript的映射关系
 *
 * @author 3y
 */
@Component
public class SmsScriptHolder {

    private Map<String, SmsScript> handlers = new HashMap<>(8);

    public void putHandler(String scriptName, SmsScript handler) {
        handlers.put(scriptName, handler);
    }
    public SmsScript route(String scriptName) {
        return handlers.get(scriptName);
    }
}
