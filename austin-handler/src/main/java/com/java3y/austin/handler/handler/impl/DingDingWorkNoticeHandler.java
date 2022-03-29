package com.java3y.austin.handler.handler.impl;

import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.dto.account.DingDingWorkNoticeAccount;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.handler.handler.BaseHandler;
import com.java3y.austin.handler.handler.Handler;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private static final String DING_DING_ROBOT_ACCOUNT_KEY = "dingDingWorkNoticeAccount";
    private static final String PREFIX = "ding_ding_work_notice_";

    @Autowired
    private AccountUtils accountUtils;

    public DingDingWorkNoticeHandler() {
        channelCode = ChannelType.DING_DING_WORK_NOTICE.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {

        DingDingWorkNoticeAccount account = accountUtils.getAccount(taskInfo.getSendAccount(), DING_DING_ROBOT_ACCOUNT_KEY, PREFIX, new DingDingWorkNoticeAccount());

        return false;
    }


}

