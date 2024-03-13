package com.atguigu.educenter.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.educenter.entity.WeChatUser;
import com.atguigu.educenter.utils.WeChatUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("weChat")
public class WeChatController {
    /**
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @RequestMapping("/wxCheck")
    public String wxSignatureCheck(
        @RequestParam(value = "signature") String signature,
        @RequestParam(value = "timestamp") String timestamp,
        @RequestParam(value = "nonce") String nonce,
        @RequestParam(value = "echostr") String echostr
    ){
        System.out.println("收到微信校验请求,echostr:"+echostr);
        return echostr;
    }

    /**
     * 点击微信登录，生成登录二维码
     *
     */
    @GetMapping("/wxLogin")
    @ResponseBody
    public void wxLoginPage(HttpServletResponse response) throws IOException {
        //redirect是网课的地址，注意要转成UrlEncode格式
        String redirectUrl = URLEncoder.encode("http://76e36770.r7.cpolar.top/weChat/wxCallback","UTF-8");
        //构造二维码链接地址
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx634e30a245d39c6f&redirect_uri="
                +redirectUrl+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        //生成二维码，扫描跳转上面的地址
        // 生成二维码图片并将其转换为Base64编码的字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        QrCodeUtil.generate(url, 300, 300, "jpg", baos);
        byte[] imageData = baos.toByteArray();
        String base64Image = Base64.encodeBase64String(imageData);

        // 构建包含Base64编码的二维码图片的HTML响应
        String htmlResponse = "<html><head><title>微信登录二维码</title></head><body><img src=\"data:image/jpeg;base64," + base64Image + "\" alt=\"微信登录二维码\"></body></html>";

        // 设置响应类型为HTML
        response.setContentType("text/html");

        // 使用响应流将HTML写回客户端
        response.getWriter().write(htmlResponse);
    }

    @RequestMapping(value = "/wxCallback")
    @ResponseBody
    public String pcCallback(String code, String state, HttpServletRequest request,
                             HttpSession session) throws IOException {
        WeChatUser user = WeChatUtils.getUserInfo(code);
        System.out.println("code:"+code + ",state:"+state);
        return JSON.toJSONString(user);
    }



}
