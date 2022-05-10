package com.java3y.austin.cron.handler;

import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.dto.account.DingDingWorkNoticeAccount;
import com.java3y.austin.support.config.SupportThreadPoolConfig;
import com.java3y.austin.support.utils.AccountUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 * 刷新钉钉的access_token
 * <p>
 * https://open.dingtalk.com/document/orgapp-server/obtain-orgapp-token
 *
 * @author 3y
 */
@Service
@Slf4j
public class RefreshDingDingAccessTokenHandler {


    private static final String URL = "https://oapi.dingtalk.com/gettoken";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AccountUtils accountUtils;

    /**
     * 每小时请求一次接口刷新（以防失效)
     */
    @XxlJob("refreshAccessTokenJob")
    public void execute() {
        log.info("refreshAccessTokenJob#execute!");
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            for (int index = SendAccountConstant.START; true; index = index + SendAccountConstant.STEP) {
                DingDingWorkNoticeAccount account = accountUtils.getAccount(index, SendAccountConstant.DING_DING_WORK_NOTICE_ACCOUNT_KEY, SendAccountConstant.DING_DING_WORK_NOTICE_PREFIX, DingDingWorkNoticeAccount.class);
                if (account == null) {
                    break;
                }
                String accessToken = getAccessToken(account);
                if (StrUtil.isNotBlank(accessToken)) {
                    redisTemplate.opsForValue().set(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + index, accessToken);
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
    private String getAccessToken(DingDingWorkNoticeAccount account) {
        String accessToken = "";
        try {
            DingTalkClient client = new DefaultDingTalkClient(URL);
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey(account.getAppKey());
            req.setAppsecret(account.getAppSecret());
            req.setHttpMethod(AustinConstant.REQUEST_METHOD_GET);
            OapiGettokenResponse rsp = client.execute(req);
            accessToken = rsp.getAccessToken();
        } catch (Exception e) {
            log.error("RefreshDingDingAccessTokenHandler#getAccessToken fail:{}", Throwables.getStackTraceAsString(e));
        }
        return accessToken;
    }
}
