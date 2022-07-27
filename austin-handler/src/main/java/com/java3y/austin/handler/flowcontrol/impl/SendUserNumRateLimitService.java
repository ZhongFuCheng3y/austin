package com.java3y.austin.handler.flowcontrol.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.handler.enums.RateLimitStrategy;
import com.java3y.austin.handler.flowcontrol.FlowControlParam;
import com.java3y.austin.handler.flowcontrol.FlowControlService;
import com.java3y.austin.handler.flowcontrol.annotations.LocalRateLimit;

/**
 * Created by TOM
 * On 2022/7/21 17:14
 */
@LocalRateLimit(rateLimitStrategy = RateLimitStrategy.SEND_USER_NUM_RATE_LIMIT)
public class SendUserNumRateLimitService implements FlowControlService {

  /**
   * 根据渠道进行流量控制
   *
   * @param taskInfo
   * @param flowControlParam
   */
  @Override
  public Double flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam) {
    RateLimiter rateLimiter = flowControlParam.getRateLimiter();
    return rateLimiter.acquire(taskInfo.getReceiver().size());
  }
}
