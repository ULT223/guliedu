package com.atguigu.educenter.utils;

import com.alibaba.fastjson.JSON;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.WeChatUser;
import com.atguigu.educenter.entity.vo.TokenInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.IOException;

public class WeChatUtils {
    public static WeChatUser getUserInfo(String code) throws IOException {
        //构造http请求客户端
        HttpClient httpClient = HttpClients.createDefault();
        //用code交换token，code为扫码后微信服务器响应来的值
        String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appId=" +
                "wx634e30a245d39c6f&secret=" +
                "2406d745a9e2b86e291ae97a3ea01146&code=" +code
                +"&grant_type=authorization_code";
        //发请求
        HttpGet httpGet = new HttpGet(tokenUrl);
        String responseResult = "";
        //接收返回的数据，转成utf-8格式
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == 200){
            responseResult = EntityUtils.toString(response.getEntity(),"UTF-8");
        }
        System.out.println("获取accessToken返回结果:"+responseResult);
        //将结果封装到tokenInfo对象中
        TokenInfo tokenInfo = JSON.parseObject(responseResult,TokenInfo.class);

        //用accessToken获取扫码人的信息
        String userInfoUrl = "http://api.weixin.qq.com/sns/userinfo?access_token="+tokenInfo.getAccess_token()
                +"&openid="+tokenInfo.getOpenid()+"&lang=zh_CN";
        //构造http请求客户端
        HttpGet httpGet1 = new HttpGet(userInfoUrl);
        //接收数据
        HttpResponse response1 = httpClient.execute(httpGet1);
        if (response1.getStatusLine().getStatusCode()==200){
            responseResult = EntityUtils.toString(response1.getEntity(),"UTF-8");
        }
        System.out.println("获取个人信息返回:"+responseResult);
        //将获取到的用户信息转化为WeChatUser对象
        WeChatUser weChatUser = JSON.parseObject(responseResult,WeChatUser.class);
        System.out.println(weChatUser.toString());
        return weChatUser;
    }
}
