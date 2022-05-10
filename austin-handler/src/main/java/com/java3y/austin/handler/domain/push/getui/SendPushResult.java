package com.java3y.austin.handler.domain.push.getui;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 发送消息后的返回值
 * @author 3y
 * https://docs.getui.com/getui/server/rest_v2/common_args/?id=doc-title-1
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendPushResult {
    /**
     * msg
     */
    @JSONField(name = "msg")
    private String msg;
    /**
     * code
     */
    @JSONField(name = "code")
    private Integer code;
    /**
     * data
     */
    @JSONField(name = "data")
    private JSONObject data;

}
