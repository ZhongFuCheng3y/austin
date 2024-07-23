package com.java3y.austin.common.dto.account.sms;

import lombok.*;

import java.util.Objects;

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

    /**
     * 重写equals方法
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        LinTongSmsAccount that = (LinTongSmsAccount) o;
        return url.equals(that.url) &&
                userName.equals(that.userName) &&
                password.equals(that.password);
    }

    /**
     * 重写hashCode方法
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(url, userName, password);
    }
}
