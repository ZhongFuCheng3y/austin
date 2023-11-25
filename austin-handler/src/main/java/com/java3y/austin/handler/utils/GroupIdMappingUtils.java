package com.java3y.austin.handler.utils;


import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.common.enums.EnumUtil;
import com.java3y.austin.common.enums.MessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * groupId 标识着每一个消费者组
 *
 * @author 3y
 */
public class GroupIdMappingUtils {
    private GroupIdMappingUtils() {
    }

    /**
     * 获取所有的groupIds
     * (不同的渠道不同的消息类型拥有自己的groupId)
     */
    public static List<String> getAllGroupIds() {
        List<String> groupIds = new ArrayList<>();
        for (ChannelType channelType : ChannelType.values()) {
            for (MessageType messageType : MessageType.values()) {
                groupIds.add(channelType.getCodeEn() + "." + messageType.getCodeEn());
            }
        }
        return groupIds;
    }


    /**
     * 根据TaskInfo获取当前消息的groupId
     *
     * @param taskInfo
     * @return
     */
    public static String getGroupIdByTaskInfo(TaskInfo taskInfo) {
        String channelCodeEn = EnumUtil.getEnumByCode(taskInfo.getSendChannel(), ChannelType.class).getCodeEn();
        String msgCodeEn = EnumUtil.getEnumByCode(taskInfo.getMsgType(), MessageType.class).getCodeEn();
        return channelCodeEn + "." + msgCodeEn;
    }
}
