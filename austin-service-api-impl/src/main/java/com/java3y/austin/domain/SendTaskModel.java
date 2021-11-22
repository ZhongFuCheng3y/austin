package com.java3y.austin.domain;

import com.java3y.austin.pipeline.ProcessModel;
import com.java3y.austin.pojo.TaskInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author 3y
 * @date 2021/11/22
 * @description 发送消息任务模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendTaskModel implements ProcessModel {


    /**
     * 请求类型 10:single 20:batch
     */
    private int requestType;

    /**
     * 请求参数  single 接口
     */
    private MessageParam messageParam;

    /**
     * 请求参数  batch 接口
     */
    private List<MessageParam> messageParamList;


    /**
     * 发送任务信息
     */
    private TaskInfo taskInfo;


}
