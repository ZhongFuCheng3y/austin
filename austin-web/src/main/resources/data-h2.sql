INSERT INTO MESSAGE_TEMPLATE ( ID, NAME, AUDIT_STATUS, FLOW_ID, MSG_STATUS
                             , CRON_TASK_ID, CRON_CROWD_PATH, EXPECT_PUSH_TIME, ID_TYPE, SEND_CHANNEL
                             , TEMPLATE_TYPE, MSG_TYPE, MSG_CONTENT, SEND_ACCOUNT, CREATOR
                             , UPDATOR, AUDITOR, TEAM, PROPOSER, IS_DELETED
                             , CREATED, UPDATED)
VALUES ( 1, '买一送十活动', 10, '', 10
       , NULL, '', '', 30, 30
       , 20, 20, '{"CONTENT":"6666","URL":"","TITLE":""}', 10, 'JAVA3Y'
       , 'JAVA3Y', '3Y', '公众号JAVA3Y', '三歪', 0
       , 1646274112, 1646275242);

INSERT INTO MESSAGE_TEMPLATE ( ID, NAME, AUDIT_STATUS, FLOW_ID, MSG_STATUS
                             , CRON_TASK_ID, CRON_CROWD_PATH, EXPECT_PUSH_TIME, ID_TYPE, SEND_CHANNEL
                             , TEMPLATE_TYPE, MSG_TYPE, MSG_CONTENT, SEND_ACCOUNT, CREATOR
                             , UPDATOR, AUDITOR, TEAM, PROPOSER, IS_DELETED
                             , CREATED, UPDATED)
VALUES ( 2, '校招信息', 10, '', 10
       , NULL, '', '', 50, 40
       , 20, 10, '{"CONTENT":"你已成功获取到OFFER","URL":"","TITLE":"招聘通知"}', 10, 'JAVA3Y'
       , 'JAVA3Y', '3Y', '公众号JAVA3Y', '鸡蛋', 0
       , 1646274195, 1646274195);


INSERT INTO MESSAGE_TEMPLATE ( ID, NAME, AUDIT_STATUS, FLOW_ID, MSG_STATUS
                             , CRON_TASK_ID, CRON_CROWD_PATH, EXPECT_PUSH_TIME, ID_TYPE, SEND_CHANNEL
                             , TEMPLATE_TYPE, MSG_TYPE, MSG_CONTENT, SEND_ACCOUNT, CREATOR
                             , UPDATOR, AUDITOR, TEAM, PROPOSER, IS_DELETED
                             , CREATED, UPDATED)
VALUES ( 3, '验证码通知', 10, '', 10
       , NULL, '', '', 30, 30
       , 20, 30, '{"CONTENT":"{$CONTENT}","URL":"","TITLE":""}', 10, 'JAVA3Y'
       , 'JAVA3Y', '3Y', '公众号JAVA3Y', '孙悟空', 0
       , 1646275213, 1646275213);