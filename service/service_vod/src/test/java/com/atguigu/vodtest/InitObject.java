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

        //����
        // ����API�������ò���
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("902b3c39d14471eebb220764b3ec0102");

        try {
            GetPlayInfoResponse response = client.getAcsResponse(request);
//            System.out.println(JSON.toJSONString(response));
//            // ��ӡ����Ҫ�ķ���ֵ���˴���ӡ���Ǵ˴������ RequestId
//            System.out.println(response.getRequestId());
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //���ŵ�ַ
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList){
                System.out.println("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base��Ϣ
            System.out.println("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            // ��ӡ������
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}
