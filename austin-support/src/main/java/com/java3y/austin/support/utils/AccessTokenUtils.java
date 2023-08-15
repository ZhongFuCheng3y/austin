package com.java3y.austin.support.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.CommonConstant;
import com.java3y.austin.common.constant.SendChanelUrlConstant;
import com.java3y.austin.common.dto.account.DingDingWorkNoticeAccount;
import com.java3y.austin.common.dto.account.GeTuiAccount;
import com.java3y.austin.support.dto.GeTuiTokenResultDTO;
import com.java3y.austin.support.dto.QueryTokenParamDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取第三发token工具类
 *
 * @author wuhui
 */
@Slf4j
public class AccessTokenUtils {

    /**
     * 获取钉钉 access_token
     *
     * @param account 钉钉工作消息 账号信息
     * @return 钉钉 access_token
     */
    public static String getDingDingAccessToken(DingDingWorkNoticeAccount account) {
        String accessToken = "";
        try {
            DingTalkClient client = new DefaultDingTalkClient(SendChanelUrlConstant.DING_DING_TOKEN_URL);
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey(account.getAppKey());
            req.setAppsecret(account.getAppSecret());
            req.setHttpMethod(CommonConstant.REQUEST_METHOD_GET);
            OapiGettokenResponse rsp = client.execute(req);
            accessToken = rsp.getAccessToken();
        } catch (Exception e) {
            log.error("AccessTokenUtils#getDingDingAccessToken fail:{}", Throwables.getStackTraceAsString(e));
        }
        return accessToken;
    }

    /**
     * 获取个推的 access_token
     *
     * @param account 创建个推账号时的元信息
     * @return 个推的 access_token
     */
    public static String getGeTuiAccessToken(GeTuiAccount account) {
        String accessToken = "";
        try {
            String url = SendChanelUrlConstant.GE_TUI_BASE_URL + account.getAppId() + SendChanelUrlConstant.GE_TUI_AUTH;
            String time = String.valueOf(System.currentTimeMillis());
            String digest = SecureUtil.sha256().digestHex(account.getAppKey() + time + account.getMasterSecret());
            QueryTokenParamDTO param = QueryTokenParamDTO.builder()
                    .timestamp(time)
                    .appKey(account.getAppKey())
                    .sign(digest).build();

            String body = HttpRequest.post(url).header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .body(JSON.toJSONString(param))
                    .timeout(20000)
                    .execute().body();
            GeTuiTokenResultDTO geTuiTokenResultDTO = JSON.parseObject(body, GeTuiTokenResultDTO.class);
            if (geTuiTokenResultDTO.getCode().equals(0)) {
                accessToken = geTuiTokenResultDTO.getData().getToken();
            }
        } catch (Exception e) {
            log.error("AccessTokenUtils#getGeTuiAccessToken fail:{}", Throwables.getStackTraceAsString(e));
        }
        return accessToken;
    }
}
