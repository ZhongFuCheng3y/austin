package com.java3y.austin.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationRecallRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiMessageCorpconversationRecallResponse;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.domain.LogParam;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.dto.account.DingDingWorkNoticeAccount;
import com.java3y.austin.common.dto.model.DingDingWorkContentModel;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.common.enums.SendMessageType;
import com.java3y.austin.handler.handler.BaseHandler;
import com.java3y.austin.handler.handler.Handler;
import com.java3y.austin.support.config.SupportThreadPoolConfig;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.support.utils.AccountUtils;
import com.java3y.austin.support.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 钉钉消息自定义机器人 消息处理器
 * <p>
 * https://open.dingtalk.com/document/group/custom-robot-access
 *
 * @author 3y
 */
@Slf4j
@Service
public class DingDingWorkNoticeHandler extends BaseHandler implements Handler {


    @Autowired
    private AccountUtils accountUtils;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private LogUtils logUtils;

    public DingDingWorkNoticeHandler() {
        channelCode = ChannelType.DING_DING_WORK_NOTICE.getCode();
    }

    private static final String SEND_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
    private static final String RECALL_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/recall";
    private static final String DING_DING_RECALL_KEY_PREFIX = "RECALL_";
    private static final String RECALL_BIZ_TYPE = "DingDingWorkNoticeHandler#recall";

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            DingDingWorkNoticeAccount account = accountUtils.getAccount(taskInfo.getSendAccount(), SendAccountConstant.DING_DING_WORK_NOTICE_ACCOUNT_KEY, SendAccountConstant.DING_DING_WORK_NOTICE_PREFIX, DingDingWorkNoticeAccount.class);
            OapiMessageCorpconversationAsyncsendV2Request request = assembleParam(account, taskInfo);
            String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + taskInfo.getSendAccount());
            OapiMessageCorpconversationAsyncsendV2Response response = new DefaultDingTalkClient(SEND_URL).execute(request, accessToken);

            // 发送成功后记录TaskId，用于消息撤回(支持当天的)
            if (response.getErrcode() == 0) {
                redisTemplate.opsForList().leftPush(DING_DING_RECALL_KEY_PREFIX + taskInfo.getMessageTemplateId(), String.valueOf(response.getTaskId()));
                redisTemplate.expire(DING_DING_RECALL_KEY_PREFIX + taskInfo.getMessageTemplateId(), (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000, TimeUnit.SECONDS);
                return true;
            }

            // 常见的错误 应当 关联至 AnchorState,由austin后台统一透出失败原因
            log.error("DingDingWorkNoticeHandler#handler fail!result:{},params:{}", JSON.toJSONString(response), JSON.toJSONString(taskInfo));
        } catch (Exception e) {
            log.error("DingDingWorkNoticeHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
        }
        return false;
    }

    /**
     * 拼装参数
     *
     * @param account
     * @param taskInfo
     */
    private OapiMessageCorpconversationAsyncsendV2Request assembleParam(DingDingWorkNoticeAccount account, TaskInfo taskInfo) {
        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        DingDingWorkContentModel contentModel = (DingDingWorkContentModel) taskInfo.getContentModel();
        try {
            // 接收者相关
            if (AustinConstant.SEND_ALL.equals(CollUtil.getFirst(taskInfo.getReceiver()))) {
                req.setToAllUser(true);
            } else {
                req.setUseridList(StringUtils.join(taskInfo.getReceiver(), StrUtil.C_COMMA));
            }
            req.setAgentId(Long.parseLong(account.getAgentId()));


            OapiMessageCorpconversationAsyncsendV2Request.Msg message = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            message.setMsgtype(SendMessageType.getDingDingWorkTypeByCode(contentModel.getSendType()));

            // 根据类型设置入参
            if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Text textObj = new OapiMessageCorpconversationAsyncsendV2Request.Text();
                textObj.setContent(contentModel.getContent());
                message.setText(textObj);
            }
            if (SendMessageType.IMAGE.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Image image = new OapiMessageCorpconversationAsyncsendV2Request.Image();
                image.setMediaId(contentModel.getMediaId());
                message.setImage(image);
            }
            if (SendMessageType.VOICE.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Voice voice = new OapiMessageCorpconversationAsyncsendV2Request.Voice();
                voice.setMediaId(contentModel.getMediaId());
                voice.setDuration(contentModel.getDuration());
                message.setVoice(voice);
            }
            if (SendMessageType.FILE.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.File file = new OapiMessageCorpconversationAsyncsendV2Request.File();
                file.setMediaId(contentModel.getMediaId());
                message.setFile(file);
            }
            if (SendMessageType.LINK.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Link link = new OapiMessageCorpconversationAsyncsendV2Request.Link();
                link.setText(contentModel.getContent());
                link.setTitle(contentModel.getTitle());
                link.setPicUrl(contentModel.getMediaId());
                link.setMessageUrl(contentModel.getUrl());
                message.setLink(link);
            }

            if (SendMessageType.MARKDOWN.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.Markdown markdown = new OapiMessageCorpconversationAsyncsendV2Request.Markdown();
                markdown.setText(contentModel.getContent());
                markdown.setTitle(contentModel.getTitle());
                message.setMarkdown(markdown);

            }
            if (SendMessageType.ACTION_CARD.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.ActionCard actionCard = new OapiMessageCorpconversationAsyncsendV2Request.ActionCard();
                actionCard.setTitle(contentModel.getTitle());
                actionCard.setMarkdown(contentModel.getContent());
                actionCard.setBtnOrientation(contentModel.getBtnOrientation());
                actionCard.setBtnJsonList(JSON.parseArray(contentModel.getBtns(), OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList.class));
                message.setActionCard(actionCard);

            }
            if (SendMessageType.OA.getCode().equals(contentModel.getSendType())) {
                OapiMessageCorpconversationAsyncsendV2Request.OA oa = new OapiMessageCorpconversationAsyncsendV2Request.OA();
                oa.setMessageUrl(contentModel.getUrl());
                oa.setHead(JSON.parseObject(contentModel.getDingDingOaHead(), OapiMessageCorpconversationAsyncsendV2Request.Head.class));
                oa.setBody(JSON.parseObject(contentModel.getDingDingOaBody(), OapiMessageCorpconversationAsyncsendV2Request.Body.class));
                message.setOa(oa);
            }
            req.setMsg(message);
        } catch (Exception e) {
            log.error("assembleParam fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return req;
    }


    /**
     * 在下发的时候存储了messageTemplate -> taskIdList
     * 只要还存在taskIdList，则将其去除
     *
     * @param messageTemplate
     */
    @Override
    public void recall(MessageTemplate messageTemplate) {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            try {
                DingDingWorkNoticeAccount account = accountUtils.getAccount(messageTemplate.getSendAccount(), SendAccountConstant.DING_DING_WORK_NOTICE_ACCOUNT_KEY, SendAccountConstant.DING_DING_WORK_NOTICE_PREFIX, DingDingWorkNoticeAccount.class);
                String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + messageTemplate.getSendAccount());
                while (redisTemplate.opsForList().size(DING_DING_RECALL_KEY_PREFIX + messageTemplate.getId()) > 0) {
                    String taskId = redisTemplate.opsForList().leftPop(DING_DING_RECALL_KEY_PREFIX + messageTemplate.getId());
                    DingTalkClient client = new DefaultDingTalkClient(RECALL_URL);
                    OapiMessageCorpconversationRecallRequest req = new OapiMessageCorpconversationRecallRequest();
                    req.setAgentId(Long.valueOf(account.getAgentId()));
                    req.setMsgTaskId(Long.valueOf(taskId));
                    OapiMessageCorpconversationRecallResponse rsp = client.execute(req, accessToken);
                    logUtils.print(LogParam.builder().bizType(RECALL_BIZ_TYPE).object(JSON.toJSONString(rsp)).build());
                }
            } catch (Exception e) {
                log.error("DingDingWorkNoticeHandler#recall fail:{}", Throwables.getStackTraceAsString(e));
            }
        });
    }
}

