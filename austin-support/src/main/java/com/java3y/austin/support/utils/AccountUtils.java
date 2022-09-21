package com.java3y.austin.support.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.support.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取账号信息工具类
 *
 * @author 3y
 */
@Component
public class AccountUtils {

    @Autowired
    private ConfigService config;

    /**
     * local.properties配置格式
     * (key:smsAccount)短信参数示例：[{"sms_10":{"url":"sms.tencentcloudapi.com","region":"ap-guangzhou","secretId":"AKIDhDxxxxxxxx1WljQq","secretKey":"B4hwww39yxxxrrrrgxyi","smsSdkAppId":"1423123125","templateId":"1182097","signName":"Java3y公众号","supplierId":10,"supplierName":"腾讯云"}},{"sms_20":{"url":"https://sms.yunpian.com/v2/sms/tpl_batch_send.json","apikey":"caffff8234234231b5cd7","tpl_id":"523333332","supplierId":20,"supplierName":"云片"}}]
     * (key:emailAccount)邮件参数示例：[{"email_10":{"host":"smtp.qq.com","port":465,"user":"23423423@qq.com","pass":"23423432432423423","from":"234@qq.com","starttlsEnable":true,"auth":true,"sslEnable":true}},{"email_20":{"host":"smtp.163.com","port":465,"user":"22222@163.com","pass":"23432423","from":"234324324234@163.com","starttlsEnable":false,"auth":true,"sslEnable":true}}]
     * (key:enterpriseWechatAccount)企业微信参数示例：[{"enterprise_wechat_10":{"corpId":"wwf87603333e00069c","corpSecret":"-IFWxS2222QxzPIorNV11144D915DM","agentId":10044442,"token":"rXROB3333Kf6i","aesKey":"MKZtoFxHIM44444M7ieag3r9ZPUsl"}}]
     * (key:dingDingRobotAccount) 钉钉自定义机器人参数示例：[{"ding_ding_robot_10":{"secret":"SEC9222d4768aded74114faae92229de422222fedf","webhook":"https://oapi.dingtalk.com/robot/send?access_token=8d03b6442222203d87333367328b0c3003d164715d2c6c6e56"}}]
     * (key:dingDingWorkNoticeAccount) 钉钉工作消息参数示例：[{"ding_ding_work_notice_10":{"appKey":"dingh6yyyyyyycrlbx","appSecret":"tQpvmkR863333yyyyyHP3QHyyyymy9Ao1yoL1oQX5NsdfsWHvWKbTu","agentId":"1523123123183622"}}]
     * (key:officialAccount) 微信服务号模板消息参数示例：[{"official_10":{"appId":"wxecb4693d2eef1ea7","secret":"624asdfsa1640d769ba20120821","templateId":"JHUk6eE9T5Ts7asdfsadfiKNDQsk-Q","url":"http://weixin.qq.com/download","miniProgramId":"xiaochengxuappid12345","path":"index?foo=bar"}}]
     * (key:miniProgramAccount) 微信小程序订阅消息参数示例：[{"mini_program_10":{"appId":"wxecb4693d2eef1ea7","appSecret":"6240870f4d91701640d769ba20120821","templateId":"JHUk6eE9T5TasdfCrQsk-Q","grantType":"client_credential","miniProgramState":"trial","page":"index?foo=bar"}}]
     * (key:alipayMiniProgramAccount) 支付宝小程序订阅消息参数实例：[{"alipay_mini_program_10":{"privateKey":"MIIEvQIBADANB......","alipayPublicKey":"MIIBIjANBg...","appId":"2014********7148","userTemplateId":"MDI4YzIxMDE2M2I5YTQzYjUxNWE4MjA4NmU1MTIyYmM=","page":"page/component/index"}}]
     */
    public <T> T getAccount(Integer sendAccount, String apolloKey, String prefix, Class<T> clazz) {
        String accountValues = config.getProperty(apolloKey, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY);
        JSONArray jsonArray = JSON.parseArray(accountValues);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T object = jsonObject.getObject(prefix + sendAccount, clazz);
            if (object != null) {
                return object;
            }
        }
        return null;
    }
}
