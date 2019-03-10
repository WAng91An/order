package com.wrq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by wangqian on 2019/2/15.
 */
@Component
@Data
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    /* 公众平台AppId */
    private String mpAppId;

    /* 公众平台密钥 */
    private String mpAppSecret;

    /* 开发平台AppId */
    private String openAppId;

    /* 开发平台密钥 */
    private String openAppSecret;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户密钥
     */
    private String mchKey;

    /**
     * 商户证书路径
     */
    private String keyPath;

    private String notifyUrl;

}

