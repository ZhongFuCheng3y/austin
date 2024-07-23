package com.java3y.austin.handler.domain.sms;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <span>Form File</span>
 * <p>Description</p>
 * <p>Company:QQ 752340543</p>
 *
 * @author topsuder
 * @version v1.0.0
 * @DATE 2022/11/24-15:24
 * @Description
 * @see com.java3y.austin.handler.domain.sms austin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinTongSendResult {

    Integer code;

    String message;
    @JSONField(name = "data")
    List<DataDTO> dtoList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataDTO {
        Integer code;
        String message;
        Long msgId;
        String phone;
    }
}
