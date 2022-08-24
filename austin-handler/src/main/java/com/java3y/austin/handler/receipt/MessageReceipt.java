package com.java3y.austin.handler.receipt;


import com.java3y.austin.support.config.SupportThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 拉取回执信息
 *
 * @author 3y
 */
@Component
@Slf4j
public class MessageReceipt {

    @Autowired
    private SmsPullReceipt smsPullReceipt;

    @PostConstruct
    private void init() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (true) {
                // TODO 回执这里自行打开(免得报错)
                // smsPullReceipt.pull();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
