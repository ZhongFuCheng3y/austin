package com.java3y.austin.common.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 云片账号信息
 *
 * 参数示例：
 * [{"sms_20":{"url":"https://sms.yunpian.com/v2/sms/tpl_batch_send.json","apikey":"ca55d4c8544444444444622221b5cd7","tpl_id":"533332222282","supplierId":20,"supplierName":"云片"}}]
 *
 * @author 3y
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YunPianSmsAccount {

    /**
     * apikey
     */
    private String apikey;
    /**
     * tplId
     */
    private String tplId;

    /**
     * api相关
     */
    private String url;

    /**
     * 标识渠道商Id
     */
    private Integer supplierId;

    /**
     * 标识渠道商名字
     */
    private String supplierName;
}
