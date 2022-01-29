package com.java3y.austin.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.constants.XxlJobConstant;
import com.java3y.austin.entity.XxlJobGroup;
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
        String path = xxlJobInfo.getId() == null ? xxlAddresses + XxlJobConstant.INSERT_URL
                : xxlAddresses + XxlJobConstant.UPDATE_URL;

        HttpResponse response = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();

            // 插入时需要返回Id，而更新时不需要
            if (path.contains(XxlJobConstant.INSERT_URL) && response.isOk()) {
                Integer taskId = Integer.parseInt(String.valueOf(JSON.parseObject(response.body()).get("content")));
                return BasicResultVO.success(taskId);
            } else if (response.isOk()) {
                return BasicResultVO.success();
            }
        } catch (Exception e) {
            log.error("CronTaskService#saveTask fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
    }

    @Override
    public BasicResultVO deleteCronTask(Integer taskId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", taskId);
        String path = xxlAddresses + XxlJobConstant.DELETE_URL;
        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("CronTaskService#deleteCronTask fail:{}", JSON.toJSONString(response.body()));
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
            log.error("CronTaskService#startCronTask fail:{}", JSON.toJSONString(response.body()));
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
            log.error("CronTaskService#stopCronTask fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.success();
    }

    @Override
    public BasicResultVO getGroupId(String appName, String title) {
        String path = xxlAddresses + XxlJobConstant.JOB_GROUP_PAGE_LIST;

        HashMap<String, Object> params = new HashMap<>();
        params.put("appname", appName);
        params.put("title", title);

        HttpResponse response = null;
        try {
            response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
            Integer id = JSON.parseObject(response.body()).getJSONArray("data").getJSONObject(0).getInteger("id");
            if (response.isOk() && id != null) {
                return BasicResultVO.success(id);
            }
        } catch (Exception e) {
            log.error("CronTaskService#getGroupId fail:{}", JSON.toJSONString(response.body()));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
        }
        return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR, JSON.toJSONString(response.body()));
    }

    @Override
    public BasicResultVO createGroup(XxlJobGroup xxlJobGroup) {
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(xxlJobGroup), Map.class);
        String path = xxlAddresses + XxlJobConstant.JOB_GROUP_INSERT_URL;
        HttpResponse response = HttpRequest.post(path).form(params).cookie(getCookie()).execute();
        if (!response.isOk()) {
            log.error("CronTaskService#createGroup fail:{}", JSON.toJSONString(response.body()));
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
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("userName", xxlUserName);
        hashMap.put("password", xxlPassword);
        hashMap.put("randomCode", IdUtil.fastSimpleUUID());

        String path = xxlAddresses + XxlJobConstant.LOGIN_URL;
        HttpResponse response = HttpRequest.post(path).form(hashMap).execute();
        if (response.isOk()) {
            List<HttpCookie> cookies = response.getCookies();
            StringBuilder sb = new StringBuilder();
            for (HttpCookie cookie : cookies) {
                sb.append(cookie.toString());
            }
            return sb.toString();
        }
        log.error("CronTaskService#getCookie fail:{}", JSON.parseObject(response.body()));
        return null;
    }
}
