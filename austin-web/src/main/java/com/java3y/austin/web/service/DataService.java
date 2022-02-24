package com.java3y.austin.web.service;

import com.java3y.austin.web.vo.amis.EchartsVo;
import com.java3y.austin.web.vo.amis.TimeLineItemVo;

/**
 * 数据链路追踪获取接口
 *
 * @author 3y
 */
public interface DataService {

    /**
     * 获取全链路追踪 用户维度信息
     */
    TimeLineItemVo getTraceUserInfo(String receiver);


    /**
     * 获取全链路追踪 消息模板维度信息
     */
    EchartsVo getTraceMessageTemplateInfo(String businessId);


}
