package com.java3y.austin.service.deduplication;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.google.common.collect.Lists;
import com.java3y.austin.constant.AustinConstant;
import com.java3y.austin.domain.DeduplicationParam;
import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.service.deduplication.build.BuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 3y.
 * @date 2021/12/12
 * 去重服务
 */
@Service
public class DeduplicationRuleService {


    private static final String SERVICE = "Service";
    @ApolloConfig("boss.austin")
    private Config config;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private BuilderFactory builderFactory;

    //需要去重的服务
    private static final List<String> DEDUPLICATION_LIST = Lists.newArrayList(DeduplicationConstants.CONTENT_DEDUPLICATION, DeduplicationConstants.FREQUENCY_DEDUPLICATION);

    public void duplication(TaskInfo taskInfo) {
        // 配置样例：{"contentDeduplication":{"num":1,"time":300},"frequencyDeduplication":{"num":5}}
        String deduplication = config.getProperty(DeduplicationConstants.DEDUPLICATION_RULE_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT);
        //去重
        DEDUPLICATION_LIST.forEach(
                key -> {
                    DeduplicationParam deduplicationParam = builderFactory.select(key).build(deduplication, key);
                    if (deduplicationParam != null) {
                        deduplicationParam.setTaskInfo(taskInfo);
                        DeduplicationService deduplicationService = findService(key + SERVICE);
                        deduplicationService.deduplication(deduplicationParam);
                    }
                }
        );

    }

    private DeduplicationService findService(String beanName) {
        return applicationContext.getBean(beanName, DeduplicationService.class);

    }

}
