package com.java3y.austin.support.utils;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.CustomLogListener;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.domain.AnchorInfo;
import com.java3y.austin.common.domain.LogParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 所有的日志都存在
 * @author 3y
 */
@Slf4j
@Component
public class LogUtils extends CustomLogListener {

    /**
     * 方法切面的日志 @OperationLog 所产生
     */
    @Override
    public void createLog(LogDTO logDTO) throws Exception {
        log.info(JSON.toJSONString(logDTO));
    }

    /**
     * 记录当前对象信息
     */
    public static void print(LogParam logParam) {
        logParam.setTimestamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(logParam));
    }

    /**
     * 记录打点信息
     */
    public static void print(AnchorInfo anchorInfo) {
        anchorInfo.setTimestamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(anchorInfo));
    }

    /**
     * 记录当前对象信息和打点信息
     */
    public static void print(LogParam logParam,AnchorInfo anchorInfo) {
        print(anchorInfo);
        print(logParam);
    }
}
