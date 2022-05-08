package com.java3y.austin.cron.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.dto.account.GeTuiAccount;
import com.java3y.austin.cron.dto.getui.QueryTokenParamDTO;
import com.java3y.austin.cron.dto.getui.GeTuiTokenResultDTO;
import com.java3y.austin.support.config.SupportThreadPoolConfig;
import com.java3y.austin.support.utils.AccountUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 * 刷新个推的token
 * <p>
 * https://docs.getui.com/getui/server/rest_v2/token/
 *
 * @author 3y
 */
@Service
@Slf4j
public class RefreshGeTuiAccessTokenHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AccountUtils accountUtils;

    /**
     * 每小时请求一次接口刷新（以防失效)
     */
    @XxlJob("refreshGeTuiAccessTokenJob")
    public void execute() {
        log.info("refreshGeTuiAccessTokenJob#execute!");
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            for (int index = SendAccountConstant.START; true; index = index + SendAccountConstant.STEP) {
                GeTuiAccount account = accountUtils.getAccount(index, SendAccountConstant.GE_TUI_ACCOUNT_KEY, SendAccountConstant.GE_TUI_ACCOUNT_PREFIX, GeTuiAccount.class);
                if (account == null) {
                    break;
                }
                String accessToken = getAccessToken(account);
                if (StrUtil.isNotBlank(accessToken)) {
                    redisTemplate.opsForValue().set(SendAccountConstant.GE_TUI_ACCESS_TOKEN_PREFIX + index, accessToken);
                }
            }
        });
    }

    /**
     * 获取 access_token
     *
     * @param account
     * @return
     */
    private String getAccessToken(GeTuiAccount account) {
        String accessToken = "";
        try {
            String url = "https://restapi.getui.com/v2/" + account.getAppId() + "/auth";
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
            log.error("RefreshGeTuiAccessTokenHandler#getAccessToken fail:{}", Throwables.getStackTraceAsString(e));
        }
        return accessToken;
    }

}
