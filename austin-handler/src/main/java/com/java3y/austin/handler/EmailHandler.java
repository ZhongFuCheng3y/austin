package com.java3y.austin.handler;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.google.common.base.Throwables;
import com.java3y.austin.domain.AnchorInfo;
import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.dto.ContentModel;
import com.java3y.austin.dto.EmailContentModel;
import com.java3y.austin.enums.AnchorState;
import com.java3y.austin.enums.ChannelType;
import com.java3y.austin.utils.LogUtils;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 邮件发送处理
 *
 * @author 3y
 */
@Component
@Slf4j
public class EmailHandler extends Handler {

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccount();
        try {
            MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle(),
                    emailContentModel.getContent(), true, null);
        } catch (Exception e) {
            log.error("EmailHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }
        return true;
    }



    /**
     * 获取账号信息
     * @return
     */
    private MailAccount getAccount() {
        MailAccount account = new MailAccount();
        try {
            account.setHost("smtp.qq.com").setPort(465);
            account.setUser("403686131@qq.com").setPass("cmnznhomnbtlbggi").setAuth(true);
            account.setFrom("403686131@qq.com");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setStarttlsEnable(true).setSslEnable(true).setCustomProperty("mail.smtp.ssl.socketFactory", sf);

            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (Exception e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }
}
