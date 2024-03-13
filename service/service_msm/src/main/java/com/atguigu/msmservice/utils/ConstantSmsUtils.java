package com.atguigu.msmservice.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantSmsUtils implements InitializingBean {
    @Value("${aliyun.sms.keyid}")
    private String keyId;

    @Value("${aliyun.sms.keysecret}")
    private String keySecret;

    @Value("${aliyun.sms.signname}")
    private String signName;

    @Value("${aliyun.sms.templatecode}")
    private String templateCode;

    public static String SIGN_NAME;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String TEMPLATE_CODE;

    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        SIGN_NAME = signName;
        TEMPLATE_CODE = templateCode;
    }
}
