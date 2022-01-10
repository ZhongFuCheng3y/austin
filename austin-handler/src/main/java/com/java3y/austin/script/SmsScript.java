package com.java3y.austin.script;

import com.java3y.austin.domain.SmsParam;
import com.java3y.austin.domain.SmsRecord;

import java.util.List;

/**
 * 短信脚本 接口
 * @author 3y
 */
public interface SmsScript {


    /**
     * 发送短信
     *
     * @param smsParam
     * @return 渠道商接口返回值
     */
    List<SmsRecord> send(SmsParam smsParam) throws Exception;

}
