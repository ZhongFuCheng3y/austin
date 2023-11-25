package com.java3y.austin.service.api.impl.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.common.domain.SimpleAnchorInfo;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.service.api.domain.TraceResponse;
import com.java3y.austin.service.api.service.TraceService;
import com.java3y.austin.support.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: sky
 * @Date: 2023/7/13 13:45
 * @Description: TraceServiceImpl
 * @Version 1.0.0
 */
@Service
@Primary
public class TraceServiceImpl implements TraceService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public TraceResponse traceByMessageId(String messageId) {
        if (CharSequenceUtil.isBlank(messageId)) {
            return new TraceResponse(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg(), null);
        }
        String redisMessageKey = CharSequenceUtil.join(StrUtil.COLON, AustinConstant.CACHE_KEY_PREFIX, AustinConstant.MESSAGE_ID, messageId);
        List<String> messageList = redisUtils.lRange(redisMessageKey, 0, -1);
        if (CollUtil.isEmpty(messageList)) {
            return new TraceResponse(RespStatusEnum.FAIL.getCode(), RespStatusEnum.FAIL.getMsg(), null);
        }

        // 0. 按时间排序
        List<SimpleAnchorInfo> sortAnchorList = messageList.stream().map(s -> JSON.parseObject(s, SimpleAnchorInfo.class)).sorted((o1, o2) -> Math.toIntExact(o1.getTimestamp() - o2.getTimestamp())).collect(Collectors.toList());

        return new TraceResponse(RespStatusEnum.SUCCESS.getCode(), RespStatusEnum.SUCCESS.getMsg(), sortAnchorList);
    }
}
