package com.java3y.austin.support.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.java3y.austin.common.constant.AustinConstant;
import org.springframework.stereotype.Component;

/**
 * 获取账号信息工具类
 *
 * @author 3y
 */
@Component

public class AccountUtils {

    @ApolloConfig("boss.austin")
    private Config config;

    /**
     * (key:smsAccount)短信参数示例：[{"sms_10":{"url":"sms.tencentcloudapi.com","region":"ap-guangzhou","secretId":"AKIDhDUUDfffffMEqBF1WljQq","secretKey":"B4h39yWnfffff7D2btue7JErDJ8gxyi","smsSdkAppId":"140025","templateId":"11897","signName":"Java3y公众号","supplierId":10,"supplierName":"腾讯云"}}]
     * (key:emailAccount)邮件参数示例：[{"email_10":{"host":"smtp.qq.com","port":465,"user":"403686131@qq.com","pass":"","from":"403686131@qq.com"}}]
     * (key:enterpriseWechatAccount)企业微信参数示例：[{"enterprise_wechat_10":{"corpId":"wwf87603333e00069c","corpSecret":"-IFWxS2222QxzPIorNVUQn144444D915DM","agentId":10044442,"token":"rXROB3333Kf6i","aesKey":"MKZtoFxHIM44444M7ieag3r9ZPUsl"}}]
     * (key:dingDingRobotAccount) 钉钉自定义机器人参数实例：[{"ding_ding_robot_10":{"secret":"SEC996d8d9d4768aded74114faae924f229229de444475a1c295d64fedf","webhook":"https://oapi.dingtalk.com/robot/send?access_token=8d03b644ffb6534b203d87333367328b0c3003d164715d2c6c6e56"}}]
     */
    public <T> T getAccount(Integer sendAccount, String apolloKey, String prefix, T t) {
        String accountValues = config.getProperty(apolloKey, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY);
        JSONArray jsonArray = JSON.parseArray(accountValues);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Object object = jsonObject.getObject(prefix + sendAccount, t.getClass());
            if (object != null) {
                return (T) object;
            }
        }
        return null;
    }

}
