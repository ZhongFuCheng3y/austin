package com.java3y.austin.handler.pending;

import com.dtp.common.em.QueueTypeEnum;
import com.dtp.common.em.RejectedTypeEnum;
import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import com.dtp.core.thread.ThreadPoolBuilder;
import com.java3y.austin.handler.utils.GroupIdMappingUtils;
import com.java3y.austin.support.config.ThreadPoolExecutorShutdownDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;


/**
 * 存储 每种消息类型 与 TaskPending 的关系
 *
 * @author 3y
 */
@Component
public class TaskPendingHolder {

    @Autowired
    private ThreadPoolExecutorShutdownDefinition threadPoolExecutorShutdownDefinition;


    /**
     * 线程池的参数(初始化参数)
     */
    private Integer coreSize = 2;
    private Integer maxSize = 2;
    private Integer queueSize = 100;
    private Map<String, ExecutorService> taskPendingHolder = new HashMap<>(32);

    /**
     * 获取得到所有的groupId
     */
    private static List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 给每个渠道，每种消息类型初始化一个线程池
     */
    @PostConstruct
    public void init() {
        /**
         * example ThreadPoolName:austin-im.notice
         *
         * 可以通过apollo配置：dynamic-tp-apollo-dtp.yml  动态修改线程池的信息
         */
        for (String groupId : groupIds) {
            DtpExecutor dtpExecutor = ThreadPoolBuilder.newBuilder()
                    .threadPoolName("austin-" + groupId)
                    .corePoolSize(coreSize)
                    .maximumPoolSize(maxSize)
                    .workQueue(QueueTypeEnum.LINKED_BLOCKING_QUEUE.getName(), queueSize, false)
                    .rejectedExecutionHandler(RejectedTypeEnum.CALLER_RUNS_POLICY.getName())
                    .buildDynamic();

            DtpRegistry.register(dtpExecutor, "beanPostProcessor");
            threadPoolExecutorShutdownDefinition.registryExecutor(dtpExecutor);
            taskPendingHolder.put(groupId, dtpExecutor);
        }
    }

    /**
     * 得到对应的线程池
     *
     * @param groupId
     * @return
     */
    public ExecutorService route(String groupId) {
        return taskPendingHolder.get(groupId);
    }


}
