package com.atguigu.vodtest;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;

import java.util.List;

public class InitObject {

    public static void main(String[] args) {
        DefaultProfile profile;
        profile = DefaultProfile.getProfile("cn-shanghai", "LTAI5tFVf7ZKp9E522Jdhb9V", "fiOetyPaigHO1iuR2UATeX4PnDM2Zk");
        DefaultAcsClient client = new DefaultAcsClient(profile);

        //创建
        // 创建API请求并设置参数
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("902b3c39d14471eebb220764b3ec0102");

        try {
            GetPlayInfoResponse response = client.getAcsResponse(request);
//            System.out.println(JSON.toJSONString(response));
//            // 打印您需要的返回值，此处打印的是此次请求的 RequestId
//            System.out.println(response.getRequestId());
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList){
                System.out.println("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.println("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            // 打印错误码
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}
