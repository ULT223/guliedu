package com.atguigu.educenter.entity;

import lombok.Data;

@Data
public class WeChatUser {
    /**
     * 用户唯一标识
     */
    private String openid;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户的性别
     */
    private Integer sex;
    /**
     * 国籍
     */
    private String country;
    /**
     * 个人资料中的城市
     */
    private String city;
    /**
     * 个人资料中的省份
     */
    private String province;
    /**
     * 用户头像
     */
    private String headimgurl;
    /**
     * 用户特权信息
     */
    private String privilege;
    /**
     * 只有在用户公众号绑定到微信开放平台账号后才会出现该字段
     */
    private String unionid;


    @Override
    public String toString() {
        return "WeChatUser{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", privilege='" + privilege + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
