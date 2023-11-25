package com.java3y.austin.cron.xxl.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.java3y.austin.common.constant.CommonConstant;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.cron.xxl.constants.XxlJobConstant;
import com.java3y.austin.cron.xxl.entity.XxlJobGroup;
import com.java3y.austin.cron.xxl.entity.XxlJobInfo;
import com.java3y.austin.cron.xxl.enums.*;
import com.java3y.austin.cron.xxl.service.CronTaskService;
import com.java3y.austin.support.domain.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * xxlJob工具类
 *
 * @author 3y
 */
@Component
public class XxlJobUtils {

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.jobHandlerName}")
    private String jobHandlerName;

    @Autowired
    private CronTaskService cronTaskService;

    /**
     * 构建xxlJobInfo信息
     *
     * @param messageTemplate
     * @return
     */
    public XxlJobInfo buildXxlJobInfo(MessageTemplate messageTemplate) {

        String scheduleConf = messageTemplate.getExpectPushTime();
        // 如果没有指定cron表达式，说明立即执行(给到xxl-job延迟5秒的cron表达式)
        if (messageTemplate.getExpectPushTime().equals(String.valueOf(CommonConstant.FALSE))) {
            scheduleConf = DateUtil.format(DateUtil.offsetSecond(new Date(), XxlJobConstant.DELAY_TIME), CommonConstant.CRON_FORMAT);
        }

        XxlJobInfo xxlJobInfo = XxlJobInfo.builder()
                .jobGroup(queryJobGroupId()).jobDesc(messageTemplate.getName())
                .author(messageTemplate.getCreator())
                .scheduleConf(scheduleConf)
                .scheduleType(ScheduleTypeEnum.CRON.name())
                .misfireStrategy(MisfireStrategyEnum.DO_NOTHING.name())
                .executorRouteStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
                .executorHandler(XxlJobConstant.JOB_HANDLER_NAME)
                .executorParam(String.valueOf(messageTemplate.getId()))
                .executorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name())
                .executorTimeout(XxlJobConstant.TIME_OUT)
                .executorFailRetryCount(XxlJobConstant.RETRY_COUNT)
                .glueType(GlueTypeEnum.BEAN.name())
                .triggerStatus(CommonConstant.FALSE)
                .glueRemark(CharSequenceUtil.EMPTY)
                .glueSource(CharSequenceUtil.EMPTY)
                .alarmEmail(CharSequenceUtil.EMPTY)
                .childJobId(CharSequenceUtil.EMPTY).build();

        if (Objects.nonNull(messageTemplate.getCronTaskId())) {
            xxlJobInfo.setId(messageTemplate.getCronTaskId());
        }
        return xxlJobInfo;
    }

    /**
     * 根据就配置文件的内容获取jobGroupId，没有则创建
     *
     * @return
     */
    private Integer queryJobGroupId() {
        BasicResultVO<Integer> basicResultVO = cronTaskService.getGroupId(appName, jobHandlerName);
        if (Objects.isNull(basicResultVO.getData())) {
            XxlJobGroup xxlJobGroup = XxlJobGroup.builder().appname(appName).title(jobHandlerName).addressType(CommonConstant.FALSE).build();
            if (RespStatusEnum.SUCCESS.getCode().equals(cronTaskService.createGroup(xxlJobGroup).getStatus())) {
                return (int) cronTaskService.getGroupId(appName, jobHandlerName).getData();
            }
        }
        return basicResultVO.getData();
    }

}
