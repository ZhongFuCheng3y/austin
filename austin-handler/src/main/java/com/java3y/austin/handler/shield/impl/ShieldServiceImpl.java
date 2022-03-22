package com.java3y.austin.handler.shield.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.enums.ShieldType;
import com.java3y.austin.handler.shield.ShieldService;
import com.java3y.austin.support.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;

/**
 * 屏蔽服务
 */
@Service
@Slf4j
public class ShieldServiceImpl implements ShieldService {

    private static final String NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY = "night_shield_send";
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void shield(TaskInfo taskInfo) {

        /**
         * example:当消息下发至austin平台时，已经是凌晨1点，业务希望此类消息在次日的早上9点推送
         * (配合 分布式任务定时任务框架搞掂)
         */
        if (isNight() && isNightShieldType(taskInfo.getShieldType())) {
            if (ShieldType.NIGHT_SHIELD_BUT_NEXT_DAY_SEND.getCode().equals(taskInfo.getShieldType())) {
                redisUtils.lPush(NIGHT_SHIELD_BUT_NEXT_DAY_SEND_KEY, JSON.toJSONString(taskInfo), (DateUtil.offsetDay(new Date(), 1).getTime()) / 1000);
            }
            taskInfo.setReceiver(new HashSet<>());
        }
    }


    /**
     * 根据code判断是否为夜间屏蔽类型
     */
    private boolean isNightShieldType(Integer code) {
        if (ShieldType.NIGHT_SHIELD.getCode().equals(code)
                || ShieldType.NIGHT_SHIELD_BUT_NEXT_DAY_SEND.getCode().equals(code)) {
            return true;
        }
        return false;
    }

    /**
     * 小时 < 8 默认就认为是凌晨(夜晚)
     * @return
     */
    private boolean isNight() {
        return Integer.valueOf(DateFormatUtils.format(new Date(), "HH")) < 8;
    }

}
