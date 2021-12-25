package com.java3y.austin.controller;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApolloController {

    @ApolloConfig("boss.austin")
    private Config config;

    @RequestMapping("/apollo")
    public String testApollo() {
        return config.getProperty("a", "b");
    }
}
