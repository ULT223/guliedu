package com.atguigu.vod.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 创建客户端对象
 */
public class InitVodClient {
    public static DefaultAcsClient initVodClient(String accessKeyId,String accessKeySecret){
        String regionId = "cn-shanghai";
        DefaultProfile profile = DefaultProfile.getProfile(regionId,accessKeyId,accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
