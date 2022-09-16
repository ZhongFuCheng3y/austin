package com.java3y.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author jwq
 * 支付宝小程序订阅消息内容
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlipayMiniProgramContentModel extends ContentModel{

    /**
     * 模板消息发送的数据
     */
    Map<String, String> map;

}
