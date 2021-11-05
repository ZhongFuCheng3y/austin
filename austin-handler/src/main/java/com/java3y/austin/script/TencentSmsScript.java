package com.java3y.austin.script;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.pojo.SmsParam;
import com.java3y.austin.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 3y
 * @date 2021/11/4
 * <p>
 * 接入文档：https://cloud.tencent.com/document/api/382/55981
 */
@Slf4j
@Service
public class TencentSmsScript {

    @Autowired
    private OkHttpUtils okHttpUtils;

    private static final String URL = "https://sms.tencentcloudapi.com/";
    private static final String ACTION = "SendSms";
    private static final String VERSION = "2021-01-11";
    private static final String SMS_SDK_APP_ID = "1400592125";
    private static final String TEMPLATE_ID = "1182097";
    private static final String SIGN_NAME = "Java3y公众号";
    private static final List<String> REGION = Arrays.asList("ap-beijing", "ap-guangzhou", "ap-nanjing");



    /**
     * 加密签名相关
     */
    private static final String AUTHORIZATION_SIGN = "TC3-HMAC-SHA256";
    private static final String CREDENTIAL = "Credential=AKIDEXAMPLE";
    private static final String service = "sms";
    private static final String TC3_REQUEST = "tc3_request";
    private static final String SIGNED_HEADERS = "SignedHeaders=content-type;host";






    public String send(SmsParam smsParam) {

        Map<String, String> header = getHeader();
        Map<String, Object> params = getParams(smsParam);
        String paramsJSON = JSON.toJSONString(params);

        String result = okHttpUtils.doPostJsonWithHeaders(URL, paramsJSON, header);


        return result;
    }

    private Map<String, Object> getParams(SmsParam smsParam) {
        HashMap<String, Object> params = new HashMap<>();
        int phoneSize = smsParam.getPhones().size() - 1;
        int paramSize = Arrays.asList(smsParam.getContent()).size() - 1;

//        params.put("PhoneNumberSet", CollUtil.join(smsParam.getPhones(), StrUtil.COMMA));
        params.put("PhoneNumberSet."+phoneSize, JSON.toJSONString(smsParam.getPhones()));
        params.put("SmsSdkAppId", SMS_SDK_APP_ID);
        params.put("TemplateId", TEMPLATE_ID);
        params.put("SignName", SIGN_NAME);
        params.put("TemplateParamSet."+paramSize, JSON.toJSONString(Arrays.asList(smsParam.getContent())));
        params.put("SessionContext", IdUtil.simpleUUID());
        return params;
    }

    private Map<String, String> getHeader() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-TC-Action", ACTION);
        headers.put("X-TC-Version", VERSION);
        headers.put("X-TC-Region", REGION.get(RandomUtil.randomInt(REGION.size())));
        headers.put("X-TC-Timestamp", String.valueOf(DateUtil.currentSeconds()));
        return headers;
    }



}
