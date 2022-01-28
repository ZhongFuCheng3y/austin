package com.java3y.austin.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.constants.XxlJobConstant;
import com.java3y.austin.entity.XxlJobInfo;
import com.java3y.austin.enums.RespStatusEnum;
import com.java3y.austin.service.CronTaskService;
import com.java3y.austin.vo.BasicResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 3y
 */
@Slf4j
@Service
public class CronTaskServiceImpl implements CronTaskService {

    @Value("${xxl.job.admin.username}")
    private String xxlUserName;

    @Value("${xxl.job.admin.password}")
    private String xxlPassword;

    @Value("${xxl.job.admin.addresses}")
    private String xxlAddresses;


    @Override
    public BasicResultVO saveCronTask(XxlJobInfo xxlJobInfo) {
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobInfo), Map.class);

        String path;
        if (xxlJobInfo.getId() == null) {
            path = xxlAddresses + XxlJobConstant.INSERT_URL;
        } else {
            path = xxlAddresses + XxlJobConstant.UPDATE_URL;
        }
        //XxlJobInfo.builder().jobGroup(1).jobDesc()
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("jobGroup", 1);
//        paramMap.put("jobDesc", "这是测试任务");
//        paramMap.put("executorRouteStrategy", "FIRST");
//        paramMap.put("cronGen_display", "* * * * * ? *");
//        paramMap.put("scheduleConf", "* * * * * ? *");
//        paramMap.put("year", "2");
//        paramMap.put("misfireStrategy", "DO_NOTHING");
//        paramMap.put("glueType", "BEAN");
//        paramMap.put("schedule_conf_CRON", "* * * * * ? *");
//        paramMap.put("executorHandler", "messageJob"); // 此处hander需提前在项目中定义
//        paramMap.put("executorBlockStrategy", "SERIAL_EXECUTION");
//        paramMap.put("executorTimeout", 0);
//        paramMap.put("executorFailRetryCount", 1);
//        paramMap.put("author", "admin");
//        paramMap.put("glueRemark", "GLUE代码初始化");
//        paramMap.put("triggerStatus", 1);
//        paramMap.put("scheduleType", "CRON");

        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("TaskService#saveTask fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.success(JSON.parseObject(response.body()));
    }

    @Override
    public BasicResultVO deleteCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        String path = xxlAddresses + XxlJobConstant.DELETE_URL;
        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("TaskService#deleteCronTask fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.success();
    }

    @Override
    public BasicResultVO startCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        String path = xxlAddresses + XxlJobConstant.RUN_URL;
        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("TaskService#startCronTask fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.success();
    }

    @Override
    public BasicResultVO stopCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);

        String path = xxlAddresses + XxlJobConstant.STOP_URL;
        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("TaskService#stopCronTask fail:{}", JSON.parseObject(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.success();
    }

    /**
     * 获取xxl cookie
     *
     * @return String
     */
    private String getCookie() {
        String path = xxlAddresses + XxlJobConstant.LOGIN_URL;
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("userName", xxlUserName);
        hashMap.put("password", xxlPassword);
        hashMap.put("randomCode", IdUtil.fastSimpleUUID());
        log.info("TaskService#getCookie params：{}", hashMap);

        HttpResponse response = HttpRequest.post(path).form(hashMap).execute();
        if (response.isOk()) {
            List<HttpCookie> cookies = response.getCookies();
            StringBuilder sb = new StringBuilder();
            for (HttpCookie cookie : cookies) {
                sb.append(cookie.toString());
            }
            return sb.toString();
        }
        log.error("TaskService#getCookie fail:{}", JSON.parseObject(response.body()));
        return null;
    }
}
