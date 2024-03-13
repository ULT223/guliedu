package com.atguigu.educenter.controller;

import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CreditCardNumber
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    //2 获取扫描人信息，添加数据
    @GetMapping("callback")
    public String callback(String code,String state){
        try {
            System.out.println("code:"+code);
            System.out.println("state:" + state);
            //1 获取code值，临时票据，类似于验证码

            //2 拿着code请求 微信固定的地址，得到两个值 access_token 和 openid
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                    "appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个参数:id 密钥 和 code值
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //System.out.println("accessTokenInfo:"+accessTokenInfo);
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo,HashMap.class);
            String accessToken = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");


            //把扫描人信息添加到数据库
            //判断数据表里面是否存在相同微信信息
            UcenterMember member = memberService.getOpenIdMember(openid);
            if (member == null){
                //拿着得到access_token和openid，再去请求微信提供固定的地址，获取到扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "http://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl  = String.format(
                        baseUserInfoUrl,
                        accessToken,
                        openid
                );
                String userInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println(userInfo);
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname =  (String) userInfoMap.get("nickname");
                String headimgurl = (String)userInfoMap.get("headimgurl");
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }
            //使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //返回首页面
            return "redirect:http://localhost:3000?token="+jwtToken;
        }catch (Exception e){
            throw new GuliException(20001,"登录失败!");
        }
    }

    //1 生成微信扫描二维码
    @GetMapping("login")
    public String getWxCode(){

        //固定地址，后面拼接参数
//        String url = "http://open.weixin.qq.com/" +
//                "connect/qrconnect?appid=" + ConstantWxUtils.WX_OPEN_APP_ID + "&response_type=code";

        //微信开放平台授权baseUrl
        String baseUrl = "http://open.weixin.qq.com/connect/qrconnect"+
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对redirect_url进行URLEncoder编码
        String redirect_url = ConstantWxUtils.WX_OPEN_APP_REDIRECT_URL;
        try {
            URLEncoder.encode(redirect_url,"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        //设置%s里面值
        String url = String.format(baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirect_url,
                "atguigu");
        //重定向到请求微信地址里面
        return "redirect:"+url;
    }
}
