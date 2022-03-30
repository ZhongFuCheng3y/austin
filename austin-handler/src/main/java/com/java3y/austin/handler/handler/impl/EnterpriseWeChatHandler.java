package com.java3y.austin.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.dto.model.EnterpriseWeChatContentModel;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.handler.handler.BaseHandler;
import com.java3y.austin.handler.handler.Handler;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxMpErrorMsgEnum;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 3y
 * 企业微信推送处理
 */
@Component
@Slf4j
public class EnterpriseWeChatHandler extends BaseHandler implements Handler {

    /**
     * 切割userId 的分隔符
     */
    private static final String DELIMITER = "|";

    @Autowired
    private AccountUtils accountUtils;

    public EnterpriseWeChatHandler() {
        channelCode = ChannelType.ENTERPRISE_WE_CHAT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            WxCpDefaultConfigImpl accountConfig = accountUtils.getAccount(taskInfo.getSendAccount(), SendAccountConstant.ENTERPRISE_WECHAT_ACCOUNT_KEY, SendAccountConstant.ENTERPRISE_WECHAT_PREFIX, new WxCpDefaultConfigImpl());
            WxCpMessageServiceImpl messageService = new WxCpMessageServiceImpl(initService(accountConfig));
            WxCpMessageSendResult result = messageService.send(buildWxCpMessage(taskInfo, accountConfig.getAgentId()));
            if (Integer.valueOf(WxMpErrorMsgEnum.CODE_0.getCode()).equals(result.getErrCode())) {
                return true;
            }
            // 常见的错误 应当 关联至 AnchorState,由austin后台统一透出失败原因
            log.error("EnterpriseWeChatHandler#handler fail!result:{},params:{}", JSON.toJSONString(result), JSON.toJSONString(taskInfo));
        } catch (Exception e) {
            log.error("EnterpriseWeChatHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }



    /**
     * 初始化 WxCpServiceImpl 服务接口
     *
     * @param config
     * @return
     */
    private WxCpService initService(WxCpDefaultConfigImpl config) {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        return wxCpService;
    }

    /**
     * 构建企业微信下发消息的对象
     *
     * @param taskInfo
     * @param agentId  应用ID
     * @return
     */
    private WxCpMessage buildWxCpMessage(TaskInfo taskInfo, Integer agentId) {
        String userId;
        if (AustinConstant.SEND_ALL.equals(CollUtil.getFirst(taskInfo.getReceiver()))) {
            userId = CollUtil.getFirst(taskInfo.getReceiver());
        } else {
            userId = StringUtils.join(taskInfo.getReceiver(), DELIMITER);
        }
        EnterpriseWeChatContentModel enterpriseWeChatContentModel = (EnterpriseWeChatContentModel) taskInfo.getContentModel();
        return WxCpMessage
                .TEXT()
                .agentId(agentId)
                .toUser(userId)
                .content(enterpriseWeChatContentModel.getContent())
                .build();
    }

}

