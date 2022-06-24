package com.java3y.austin.handler.handler.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.dto.account.EnterpriseWeChatRobotAccount;
import com.java3y.austin.common.dto.account.FeiShuRobotAccount;
import com.java3y.austin.common.dto.model.EnterpriseWeChatRobotContentModel;
import com.java3y.austin.common.dto.model.FeiShuRobotContentModel;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.common.enums.SendMessageType;
import com.java3y.austin.handler.domain.feishu.FeiShuRobotParam;
import com.java3y.austin.handler.domain.feishu.FeiShuRobotResult;
import com.java3y.austin.handler.domain.wechat.robot.EnterpriseWeChatRobotParam;
import com.java3y.austin.handler.handler.BaseHandler;
import com.java3y.austin.handler.handler.Handler;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业微信群机器人 消息处理器
 *
 * @author 3y
 */
@Slf4j
@Service
public class FeiShuRobotHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    public FeiShuRobotHandler() {
        channelCode = ChannelType.FEI_SHU_ROBOT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            FeiShuRobotAccount account = accountUtils.getAccount(taskInfo.getSendAccount(), SendAccountConstant.FEI_SHU_ROBOT_ACCOUNT_KEY, SendAccountConstant.FEI_SHU_ROBOT_PREFIX, FeiShuRobotAccount.class);
            FeiShuRobotParam feiShuRobotParam = assembleParam(taskInfo);
            String result = HttpRequest.post(account.getWebhook())
                    .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .body(JSON.toJSONString(feiShuRobotParam))
                    .timeout(2000)
                    .execute().body();
            FeiShuRobotResult feiShuRobotResult = JSON.parseObject(result, FeiShuRobotResult.class);
            if (feiShuRobotResult.getStatusCode() == 0) {
                return true;
            }
            log.error("FeiShuRobotHandler#handler fail! result:{},params:{}", JSON.toJSONString(feiShuRobotResult), JSON.toJSONString(taskInfo));
        } catch (Exception e) {
            log.error("FeiShuRobotHandler#handler fail!e:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    private FeiShuRobotParam assembleParam(TaskInfo taskInfo) {
        FeiShuRobotContentModel contentModel = (FeiShuRobotContentModel) taskInfo.getContentModel();

        FeiShuRobotParam param = FeiShuRobotParam.builder()
                .msgType(SendMessageType.geFeiShuRobotTypeByCode(contentModel.getSendType())).build();

        if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
            param.setContent(FeiShuRobotParam.ContentDTO.builder().text(contentModel.getContent()).build());
        }
        if (SendMessageType.RICH_TEXT.getCode().equals(contentModel.getSendType())) {
            List<FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.PostContentDTO> postContentDTOS = JSON.parseArray(contentModel.getPostContent(), FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.PostContentDTO.class);
            List<List<FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.PostContentDTO>> postContentList = new ArrayList<>();
            postContentList.add(postContentDTOS);
            FeiShuRobotParam.ContentDTO.PostDTO postDTO = FeiShuRobotParam.ContentDTO.PostDTO.builder()
                    .zhCn(FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.builder().title(contentModel.getTitle()).content(postContentList).build())
                    .build();
            param.setContent(FeiShuRobotParam.ContentDTO.builder().post(postDTO).build());
        }
        if (SendMessageType.SHARE_CHAT.getCode().equals(contentModel.getSendType())) {
            param.setContent(FeiShuRobotParam.ContentDTO.builder().shareChatId(contentModel.getMediaId()).build());
        }
        if (SendMessageType.IMAGE.getCode().equals(contentModel.getSendType())) {
            param.setContent(FeiShuRobotParam.ContentDTO.builder().imageKey(contentModel.getMediaId()).build());
        }
        if (SendMessageType.ACTION_CARD.getCode().equals(contentModel.getSendType())) {
            //
        }
        return param;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }
}

