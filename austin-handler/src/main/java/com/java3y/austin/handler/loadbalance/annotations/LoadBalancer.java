package com.java3y.austin.handler.loadbalance.annotations;

import com.java3y.austin.handler.enums.LoadBalancerStrategy;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 单机限流注解
 * Created by TOM
 * On 2022/7/21 17:03
 *
 * @author TOM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface LoadBalancer {

    String loadbalancer() default LoadBalancerStrategy.SERVICE_LOAD_BALANCER_RANDOM_WEIGHT_ENHANCED;
}
