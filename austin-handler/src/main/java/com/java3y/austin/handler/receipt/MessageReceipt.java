package com.java3y.austin.handler.receipt;


import com.google.common.base.Throwables;
import com.java3y.austin.handler.receipt.stater.ReceiptMessageStater;
import com.java3y.austin.support.config.SupportThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 拉取回执信息 入口
 *
 * @author 3y
 */
@Component
@Slf4j
public class MessageReceipt {

    @Autowired
    private List<ReceiptMessageStater> receiptMessageStaterList;

    /**
     * 是否终止线程
     */
    private volatile boolean stop = false;

    @PostConstruct
    private void init() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (!stop) {
                try {
                    for (ReceiptMessageStater receiptMessageStater : receiptMessageStaterList) {
                        //receiptMessageStater.start();
                    }
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ex) {
                    log.error("MessageReceipt#init interrupted: {}", ex.getMessage());
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("MessageReceipt#init fail:{}", Throwables.getStackTraceAsString(e));
                }
            }
        });
    }

    /**
     * 销毁调用
     */
    @PreDestroy
    public void onDestroy() {
        this.stop = true;
        SupportThreadPoolConfig.getPendingSingleThreadPool().shutdown();
    }

}
