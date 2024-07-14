DROP DATABASE IF EXISTS `austin`;

CREATE DATABASE `austin`;

USE `austin`;

CREATE TABLE IF NOT EXISTS `message_template`
(
    `id`               BIGINT(20)    NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(100)  NOT NULL DEFAULT '' COMMENT '标题',
    `audit_status`     TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '当前消息审核状态： 10.待审核 20.审核成功 30.被拒绝',
    `flow_id`          VARCHAR(50) COMMENT '工单ID',
    `msg_status`       TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '当前消息状态：10.新建 20.停用 30.启用 40.等待发送 50.发送中 60.发送成功 70.发送失败',
    `cron_task_id`     BIGINT(20) COMMENT '定时任务Id (xxl-job-admin返回)',
    `cron_crowd_path`  VARCHAR(500) COMMENT '定时发送人群的文件路径',
    `expect_push_time` VARCHAR(100) COMMENT '期望发送时间：0:立即发送 定时任务以及周期任务:cron表达式',
    `id_type`          TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '消息的发送ID类型：10. userId 20.did 30.手机号 40.openId 50.email 60.企业微信userId',
    `send_channel`     INT(10)       NOT NULL DEFAULT '0' COMMENT '消息发送渠道：10.IM 20.Push 30.短信 40.Email 50.公众号 60.小程序 70.企业微信 80.钉钉机器人 90.钉钉工作通知 100.企业微信机器人 110.飞书机器人 110. 飞书应用消息 ',
    `template_type`    TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '10.运营类 20.技术类接口调用',
    `msg_type`         TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '10.通知类消息 20.营销类消息 30.验证码类消息',
    `shield_type`      TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '10.夜间不屏蔽 20.夜间屏蔽 30.夜间屏蔽(次日早上9点发送)',
    `msg_content`      VARCHAR(4096) NOT NULL DEFAULT '' COMMENT '消息内容 占位符用{$var}表示',
    `send_account`     INT(10)       NOT NULL DEFAULT '0' COMMENT '发送账号 一个渠道下可存在多个账号',
    `creator`          VARCHAR(45)   NOT NULL DEFAULT '' COMMENT '创建者',
    `updator`          VARCHAR(45)   NOT NULL DEFAULT '' COMMENT '更新者',
    `auditor`          VARCHAR(45)   NOT NULL DEFAULT '' COMMENT '审核人',
    `team`             VARCHAR(45)   NOT NULL DEFAULT '' COMMENT '业务方团队',
    `proposer`         VARCHAR(45)   NOT NULL DEFAULT '' COMMENT '业务方',
    `is_deleted`       TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '是否删除：0.不删除 1.删除',
    `created`          INT(11)       NOT NULL DEFAULT '0' COMMENT '创建时间',
    `updated`          INT(11)       NOT NULL DEFAULT '0' COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_channel` (`send_channel`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息模板信息';

CREATE TABLE IF NOT EXISTS `sms_record`
(
    `id`                  BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `message_template_id` BIGINT(20)   NOT NULL DEFAULT '0' COMMENT '消息模板ID',
    `phone`               BIGINT(20)   NOT NULL DEFAULT '0' COMMENT '手机号',
    `supplier_id`         TINYINT(4)   NOT NULL DEFAULT '0' COMMENT '发送短信渠道商的ID',
    `supplier_name`       VARCHAR(40)  NOT NULL DEFAULT '' COMMENT '发送短信渠道商的名称',
    `msg_content`         VARCHAR(600) NOT NULL DEFAULT '' COMMENT '短信发送的内容',
    `series_id`           VARCHAR(100) NOT NULL DEFAULT '' COMMENT '下发批次的ID',
    `charging_num`        TINYINT(4)   NOT NULL DEFAULT '0' COMMENT '计费条数',
    `report_content`      VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '回执内容',
    `status`              TINYINT(4)   NOT NULL DEFAULT '0' COMMENT '短信状态： 10.发送 20.成功 30.失败',
    `send_date`           INT(11)      NOT NULL DEFAULT '0' COMMENT '发送日期：20211112',
    `created`             INT(11)      NOT NULL DEFAULT '0' COMMENT '创建时间',
    `updated`             INT(11)      NOT NULL DEFAULT '0' COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_send_date` (`send_date`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='短信记录信息';

CREATE TABLE IF NOT EXISTS `channel_account`
(
    `id`             BIGINT(20)    NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(100)  NOT NULL DEFAULT '' COMMENT '账号名称',
    `send_channel`   TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '消息发送渠道：10.IM 20.Push 30.短信 40.Email 50.公众号 60.小程序 70.企业微信 80.钉钉机器人 90.钉钉工作通知 100.企业微信机器人 110.飞书机器人 110. 飞书应用消息 ',
    `account_config` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '账号配置',
    `creator`        VARCHAR(128)  NOT NULL DEFAULT 'Java3y' COMMENT '拥有者',
    `created`        INT(11)       NOT NULL DEFAULT '0' COMMENT '创建时间',
    `updated`        INT(11)       NOT NULL DEFAULT '0' COMMENT '更新时间',
    `is_deleted`     TINYINT(4)    NOT NULL DEFAULT '0' COMMENT '是否删除：0.不删除 1.删除',
    PRIMARY KEY (`id`),
    KEY `idx_send_channel` (`send_channel`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='渠道账号信息';