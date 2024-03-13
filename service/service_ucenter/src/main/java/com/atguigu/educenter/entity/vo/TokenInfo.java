package com.atguigu.educenter.entity.vo;

import lombok.Data;

@Data
public class TokenInfo {
    /**
     * 网页授权接口调用凭证
     */
    private String access_token;

    /**
     * access_token接口调用凭证超时时间
     */
    private String expires_in;
    /**
     * 用户唯一标识
     */
    private String openid;
    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;
}
