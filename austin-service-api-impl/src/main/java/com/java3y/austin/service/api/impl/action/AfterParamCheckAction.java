package com.java3y.austin.service.api.impl.action;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.enums.IdType;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.service.api.impl.domain.SendTaskModel;
import com.java3y.austin.support.pipeline.BusinessProcess;
import com.java3y.austin.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 3y
 * <p>
 * 后置参数检查
 */
@Slf4j
@Service
public class AfterParamCheckAction implements BusinessProcess<SendTaskModel> {

    public static final String PHONE_REGEX_EXP = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";
    public static final String EMAIL_REGEX_EXP = "^[A-Za-z0-9-_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    public static final HashMap<Integer, String> CHANNEL_REGEX_EXP = new HashMap<>();
    static {
        CHANNEL_REGEX_EXP.put(IdType.PHONE.getCode(), PHONE_REGEX_EXP);
        CHANNEL_REGEX_EXP.put(IdType.EMAIL.getCode(), EMAIL_REGEX_EXP);
    }


    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        List<TaskInfo> taskInfo = sendTaskModel.getTaskInfo();

        // 1. 过滤掉不合法的手机号、邮件
        filterIllegalReceiver(taskInfo);

        if (CollUtil.isEmpty(taskInfo)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
        }
    }

    /**
     * 如果指定类型是手机号，检测输入手机号是否合法
     * 如果指定类型是邮件，检测输入邮件是否合法
     * @param taskInfo
     */
    private void filterIllegalReceiver(List<TaskInfo> taskInfo) {
        Integer idType = CollUtil.getFirst(taskInfo.iterator()).getIdType();
        filter(taskInfo, CHANNEL_REGEX_EXP.get(idType));
    }

    /**
     * 利用正则过滤掉不合法的接收者
     *
     * @param taskInfo
     * @param regexExp
     */
    private void filter(List<TaskInfo> taskInfo, String regexExp) {
        Iterator<TaskInfo> iterator = taskInfo.iterator();
        while (iterator.hasNext()) {
            TaskInfo task = iterator.next();
            Set<String> illegalPhone = task.getReceiver().stream()
                    .filter(phone -> !ReUtil.isMatch(regexExp, phone))
                    .collect(Collectors.toSet());

            if (CollUtil.isNotEmpty(illegalPhone)) {
                task.getReceiver().removeAll(illegalPhone);
                log.error("messageTemplateId:{} find illegal receiver!{}", task.getMessageTemplateId(), JSON.toJSONString(illegalPhone));
            }
            if (CollUtil.isEmpty(task.getReceiver())) {
                iterator.remove();
            }
        }
    }

}
