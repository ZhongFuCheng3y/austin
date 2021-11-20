package com.java3y.austin.kafkatest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author 18011618
 * @Description
 * @Date 14:50 2018/7/20
 * @Modify By
 */
@Component
@Slf4j
public class UserLogConsumer {
    @KafkaListener(topics = {"austin"},groupId = "austinGroup1")
    public void consumer(ConsumerRecord<?,?> consumerRecord){
        //判断是否为null
        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        log.info(">>>>>>>>>> record =" + kafkaMessage);
        if(kafkaMessage.isPresent()){
            //得到Optional实例中的值
            Object message = kafkaMessage.get();
            System.err.println("消费消息:"+message);
        }

    }
}