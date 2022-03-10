package com.java3y.austin.web.controller;


import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThreadPoolTest {


    @GetMapping("/tp")
    public void send() {
        DtpExecutor dtpExecutor1 = DtpRegistry.getExecutor("austin-im.notice");
        DtpExecutor dtpExecutor2 = DtpRegistry.getExecutor("execute-xxl-thread-pool");
        DtpExecutor dtpExecutor3 = DtpRegistry.getExecutor("dynamic-tp-test-2");

        System.out.println(dtpExecutor1);
        System.out.println(dtpExecutor2);
        System.out.println(dtpExecutor3);

    }
}
