package com.java3y.austin.common.dto.account.sms;

import lombok.*;

/**
 * <span>Form File</span>
 * <p>Description</p>
 * <p>Company:QQ 752340543</p>
 *
 * @author topsuder
 * @version v1.0.0
 * @DATE 2022/11/24-14:39
 * @Description
 * @see com.java3y.austin.common.dto.account austin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinTongSmsAccount extends SmsAccount {
    /**
     * api相关
     */
    private String url;

    /**
     * 账号相关
     */
    private String userName;
    private String password;

}
