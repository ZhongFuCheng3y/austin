package com.java3y.austin.handler.receipt;


import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendresultRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendresultResponse;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.dto.account.DingDingWorkNoticeAccount;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 拉取钉钉工作消息回执信息
 *
 * @author 3y
 */
@Component
@Slf4j
public class DingDingWorkReceipt {

    private static final String URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult";
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AccountUtils accountUtils;
    public void pull() {
//        try {
//            for (int index = SendAccountConstant.START; true; index = index + SendAccountConstant.STEP) {
//                DingDingWorkNoticeAccount account = accountUtils.getAccount(index, SendAccountConstant.DING_DING_WORK_NOTICE_ACCOUNT_KEY, SendAccountConstant.DING_DING_WORK_NOTICE_PREFIX, DingDingWorkNoticeAccount.class);
//                if (account == null) {
//                    break;
//                }
//                String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + index);
//                DingTalkClient client = new DefaultDingTalkClient(URL);
//                OapiMessageCorpconversationGetsendresultRequest req = new OapiMessageCorpconversationGetsendresultRequest();
//                req.setAgentId(Long.valueOf(account.getAgentId()));
//                req.setTaskId(456L);
//                OapiMessageCorpconversationGetsendresultResponse rsp = client.execute(req, accessToken);
//                System.out.println(rsp.getBody());
//            }
//        } catch (Exception e) {
//            log.error("DingDingWorkReceipt#pull");
//        }
    }
}
