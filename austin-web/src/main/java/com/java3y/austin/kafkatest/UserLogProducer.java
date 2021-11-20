package com.java3y.austin.kafkatest;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author 18011618
 * @Description
 * @Date 14:43 2018/7/20
 * @Modify By
 */
@Component
public class UserLogProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 发送数据
     * @param userid
     */
    public void sendLog(String userid){
        UserLog userLog = new UserLog();
        userLog.setUsername("jhp").setUserid(userid).setState("0");
        System.err.println("发送用户日志数据:"+userLog);
        kafkaTemplate.send("austin", JSON.toJSONString(userLog));
    }
}