package com.java3y.austin.cron.pending;

import com.java3y.austin.cron.domain.CrowdInfoVo;
import com.java3y.austin.support.pending.BatchPendingThread;
import com.java3y.austin.support.pending.Pending;
import com.java3y.austin.support.pending.PendingParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 批量处理任务信息
 *
 * @author 3y
 */
@Component
@Slf4j
public class CrowdBatchTaskPending extends Pending<CrowdInfoVo> {

    @Override
    public void initAndStart(PendingParam pendingParam) {
        threadNum = pendingParam.getThreadNum() == null ? threadNum : pendingParam.getThreadNum();
        queue = pendingParam.getQueue();

        for (int i = 0; i < threadNum; ++i) {
            BatchPendingThread<CrowdInfoVo> batchPendingThread = new BatchPendingThread();
            batchPendingThread.setPendingParam(pendingParam);
            batchPendingThread.setName("batchPendingThread-" + i);
            batchPendingThread.start();
        }
    }

    @Override
    public void doHandle(List<CrowdInfoVo> list) {
        log.info("theadName:{},doHandle:{}", Thread.currentThread().getName(), list.size());

    }
}
