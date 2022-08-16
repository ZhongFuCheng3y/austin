drop table if exists channel_account CASCADE;
drop table if exists message_template CASCADE;
drop table if exists sms_record CASCADE;
CREATE TABLE message_template
(
    id               INT   NOT NULL AUTO_INCREMENT,
    name             VARCHAR(100) NOT NULL DEFAULT '' COMMENT '标题',
    audit_status     TINYINT   NOT NULL DEFAULT '0' COMMENT '当前消息审核状态： 10.待审核 20.审核成功 30.被拒绝',
    flow_id          VARCHAR(50) COMMENT '工单ID',
    msg_status       TINYINT   NOT NULL DEFAULT '0' COMMENT '当前消息状态：10.新建 20.停用 30.启用 40.等待发送 50.发送中 60.发送成功 70.发送失败',
    cron_task_id     BIGINT COMMENT '定时任务ID (XXL-JOB-ADMIN返回)',
    cron_crowd_path  VARCHAR(500) COMMENT '定时发送人群的文件路径',
    expect_push_time VARCHAR(100) COMMENT '期望发送时间：0:立即发送 定时任务以及周期任务:CRON表达式',
    id_type          TINYINT   NOT NULL DEFAULT '0' COMMENT '消息的发送ID类型：10. USERID 20.DID 30.手机号 40.OPENID 50.EMAIL 60.企业微信USERID',
    send_channel     TINYINT   NOT NULL DEFAULT '0' COMMENT '消息发送渠道：10.IM 20.PUSH 30.短信 40.EMAIL 50.公众号 60.小程序 70.企业微信 80.钉钉机器人 90.钉钉工作通知 100.企业微信机器人 110.飞书机器人 110. 飞书应用消息 ',
    template_type    TINYINT   NOT NULL DEFAULT '0' COMMENT '10.运营类 20.技术类接口调用',
    msg_type         TINYINT   NOT NULL DEFAULT '0' COMMENT '10.通知类消息 20.营销类消息 30.验证码类消息',
    shield_type      TINYINT   NOT NULL DEFAULT '0' COMMENT '10.夜间不屏蔽 20.夜间屏蔽 30.夜间屏蔽(次日早上9点发送)',
    msg_content      VARCHAR(600) NOT NULL DEFAULT '' COMMENT '消息内容 占位符用{$VAR}表示',
    send_account     TINYINT  NOT NULL DEFAULT '0' COMMENT '发送账号 一个渠道下可存在多个账号',
    creator          VARCHAR(45)  NOT NULL DEFAULT '' COMMENT '创建者',
    updator          VARCHAR(45)  NOT NULL DEFAULT '' COMMENT '更新者',
    auditor          VARCHAR(45)  NOT NULL DEFAULT '' COMMENT '审核人',
    team             VARCHAR(45)  NOT NULL DEFAULT '' COMMENT '业务方团队',
    proposer         VARCHAR(45)  NOT NULL DEFAULT '' COMMENT '业务方',
    is_deleted       TINYINT  NOT NULL DEFAULT '0' COMMENT '是否删除：0.不删除 1.删除',
    created          INT      NOT NULL DEFAULT '0' COMMENT '创建时间',
    updated          INT      NOT NULL DEFAULT '0' COMMENT '更新时间'
);
CREATE TABLE SMS_RECORD
(
    id                  BIGINT   NOT NULL AUTO_INCREMENT,
    message_template_id BIGINT   NOT NULL DEFAULT '0' COMMENT '消息模板ID',
    phone               BIGINT   NOT NULL DEFAULT '0' COMMENT '手机号',
    supplier_id         TINYINT   NOT NULL DEFAULT '0' COMMENT '发送短信渠道商的ID',
    supplier_name       VARCHAR(40)  NOT NULL DEFAULT '' COMMENT '发送短信渠道商的名称',
    msg_content         VARCHAR(600) NOT NULL DEFAULT '' COMMENT '短信发送的内容',
    series_id           VARCHAR(100) NOT NULL DEFAULT '' COMMENT '下发批次的ID',
    charging_num        TINYINT   NOT NULL DEFAULT '0' COMMENT '计费条数',
    report_content      VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '回执内容',
    status              TINYINT   NOT NULL DEFAULT '0' COMMENT '短信状态： 10.发送 20.成功 30.失败',
    send_date           INT      NOT NULL DEFAULT '0' COMMENT '发送日期：20211112',
    created             INT      NOT NULL DEFAULT '0' COMMENT '创建时间',
    updated             INT      NOT NULL DEFAULT '0' COMMENT '更新时间'
);
CREATE TABLE CHANNEL_ACCOUNT
(
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    name           VARCHAR(100)  NOT NULL DEFAULT '' COMMENT '账号名称',
    send_channel   TINYINT       NOT NULL DEFAULT '0' COMMENT '消息发送渠道：10.IM 20.PUSH 30.短信 40.EMAIL 50.公众号 60.小程序 70.企业微信 80.钉钉机器人 90.钉钉工作通知 100.企业微信机器人 110.飞书机器人 110. 飞书应用消息 ',
    account_config VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '账号配置',
    created        INT           NOT NULL DEFAULT '0' COMMENT '创建时间',
    updated        INT           NOT NULL DEFAULT '0' COMMENT '更新时间',
    is_deleted     TINYINT       NOT NULL DEFAULT '0' COMMENT '是否删除：0.不删除 1.删除'
);
