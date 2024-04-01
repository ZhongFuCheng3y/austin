package com.java3y.austin.handler.deduplication.limit;

import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.handler.deduplication.DeduplicationParam;
import com.java3y.austin.handler.deduplication.service.AbstractDeduplicationService;

import java.util.Set;

/**
 * @author cao
 * @date 2022-04-20 11:58
 * 去重的本质确实就是 param.time 内达到 param.num 次
 * <p>
 * 1，SlideWindowLimitService 业务逻辑： 5分钟内相同用户如果收到相同的内容，则应该被过滤掉 ，由lua脚本实现
 * 2，SimpleLimitService 业务逻辑： 一天内相同的用户如果已经收到某渠道内容5次，则应该被过滤掉 ，由pipeline & mget实现
 * <p>
 * 现有这两种业务 都可以用lua脚本来实现，只不过 一天内相同的用户如果已经收到某渠道内容5次，则应该被过滤掉 这个业务逻辑相对简单，对一致性的要求也没那么高。
 * 该业务场景下 pipeline & mget 够用了，所以就没上lua脚本了。
 * <p>
 * （pipeline&mget相对灵活些，吞吐量相对会大点）
 */
public interface LimitService {


    /**
     * 去重限制
     *
     * @param service  去重器对象
     * @param taskInfo
     * @param param    去重参数
     * @return 返回不符合条件的手机号码
     */
    Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param);

}
