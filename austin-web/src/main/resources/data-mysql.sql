
-- 实时类型 短信（无占位符）
INSERT INTO austin.message_template (id, name, audit_status, flow_id, msg_status, cron_task_id, cron_crowd_path,
                                     expect_push_time, id_type, send_channel, template_type, msg_type, msg_content,
                                     send_account, creator, updator, auditor, team, proposer, is_deleted, created,
                                     updated)
VALUES (1, '买一送十活动', 10, '', 10, null, '', '', 30, 30, 20, 20, '{"content":"6666","url":"","title":""}', 10, 'Java3y',
        'Java3y', '3y', '公众号Java3y', '三歪', 0, 1646274112, 1646275242);

-- 实时类型 邮件（无占位符）
INSERT INTO austin.message_template (id, name, audit_status, flow_id, msg_status, cron_task_id, cron_crowd_path,
                                     expect_push_time, id_type, send_channel, template_type, msg_type, msg_content,
                                     send_account, creator, updator, auditor, team, proposer, is_deleted, created,
                                     updated)
VALUES (2, '校招信息', 10, '', 10, null, '', '', 50, 40, 20, 10, '{"content":"你已成功获取到offer","url":"","title":"招聘通知"}', 10,
        'Java3y', 'Java3y', '3y', '公众号Java3y', '鸡蛋', 0, 1646274195, 1646274195);

-- 实时类型 短信（有占位符）占位符key 为 content
INSERT INTO austin.message_template (id, name, audit_status, flow_id, msg_status, cron_task_id, cron_crowd_path,
                                     expect_push_time, id_type, send_channel, template_type, msg_type, msg_content,
                                     send_account, creator, updator, auditor, team, proposer, is_deleted, created,
                                     updated)
VALUES (3, '验证码通知', 10, '', 10, null, '', '', 30, 30, 20, 30, '{"content":"{$content}","url":"","title":""}', 10,
        'Java3y', 'Java3y', '3y', '公众号Java3y', '孙悟空', 0, 1646275213, 1646275213);

