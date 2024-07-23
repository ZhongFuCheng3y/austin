package com.java3y.austin.common.dto.account.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 腾讯短信参数
 * <p>
 * 账号参数示例：
 * {
 * "url": "sms.tencentcloudapi.com",
 * "region": "ap-guangzhou",
 * "secretId": "AKIDhDxxxxxxxx1WljQq",
 * "secretKey": "B4hwww39yxxxrrrrgxyi",
 * "smsSdkAppId": "1423123125",
 * "templateId": "1182097",
 * "signName": "Java3y公众号",
 * "supplierId": 10,
 * "supplierName": "腾讯云",
 * "scriptName": "TencentSmsScript"
 * }
 *
 * @author 3y
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TencentSmsAccount extends SmsAccount {

    /**
     * api相关
     */
    private String url;
    private String region;

    /**
     * 账号相关
     */
    private String secretId;
    private String secretKey;
    private String smsSdkAppId;
    private String templateId;
    private String signName;

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
        TencentSmsAccount that = (TencentSmsAccount) o;
        return url.equals(that.url) &&
                region.equals(that.region) &&
                secretId.equals(that.secretId) &&
                secretKey.equals(that.secretKey) &&
                smsSdkAppId.equals(that.smsSdkAppId) &&
                templateId.equals(that.templateId) &&
                signName.equals(that.signName);
    }

    /**
     * 重写hashCode方法
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), url, region, secretId, secretKey, smsSdkAppId, templateId, signName);
    }
}
