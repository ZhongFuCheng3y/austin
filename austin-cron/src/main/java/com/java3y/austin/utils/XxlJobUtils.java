package com.java3y.austin.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.constant.AustinConstant;
import com.java3y.austin.constants.XxlJobConstant;
import com.java3y.austin.domain.MessageTemplate;
import com.java3y.austin.entity.XxlJobInfo;
import com.java3y.austin.enums.*;

/**
 * xxlJob工具类
 *
 * @author 3y
 */
public class XxlJobUtils {

    /**
     * 构建xxlJobInfo信息
     *
     * @param messageTemplate
     * @param triggerStatus 是否启动定时任务
     * @return
     */
    public static XxlJobInfo buildXxlJobInfo(MessageTemplate messageTemplate) {

        // 判断是否为cron表达式
        String scheduleConf = StrUtil.EMPTY;
        String scheduleType = ScheduleTypeEnum.NONE.name();
        if (!messageTemplate.getExpectPushTime().equals(String.valueOf(AustinConstant.FALSE))) {
            scheduleType = ScheduleTypeEnum.CRON.name();
            scheduleConf = messageTemplate.getExpectPushTime();
        }

        XxlJobInfo xxlJobInfo = XxlJobInfo.builder().jobGroup(1).jobDesc(messageTemplate.getName())
                .author(messageTemplate.getCreator())
                .scheduleConf(scheduleConf)
                .scheduleType(scheduleType)
                .misfireStrategy(MisfireStrategyEnum.DO_NOTHING.name())
                .executorBlockStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
                .executorHandler(XxlJobConstant.HANDLER_NAME)
                .executorParam(JSON.toJSONString(messageTemplate))
                .executorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name())
                .executorTimeout(XxlJobConstant.TIME_OUT)
                .executorFailRetryCount(XxlJobConstant.RETRY_COUNT)
                .glueType(GlueTypeEnum.BEAN.name())
                .triggerStatus(AustinConstant.FALSE)
                .glueRemark(StrUtil.EMPTY)
                .glueSource(StrUtil.EMPTY)
                .alarmEmail(StrUtil.EMPTY)
                .childJobId(StrUtil.EMPTY).build();

        if (messageTemplate.getCronTaskId() != null) {
            xxlJobInfo.setId(messageTemplate.getCronTaskId());
        }
        return xxlJobInfo;
    }
}
