package com.atguigu.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TestVod {
    public static void main(String[] args) throws ClientException {
        getPlayAuth();
    }

    /**
     *
     */
    public  static void uploadVideo(){
        String accessKeyId = "LTAI5tFVf7ZKp9E522Jdhb9V";
        String accessKeySecret = "fiOetyPaigHO1iuR2UATeX4PnDM2Zk";
        String title = "Call Me MayBe";
        String fileName = "G:\\Work\\Call Me Maybe.mp4";
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        /* 是否开启断点续传, 默认断点续传功能关闭。当网络不稳定或者程序崩溃时，再次发起相同上传请求，可以继续未完成的上传任务，适用于超时3000秒仍不能上传完成的大文件。
        注意: 断点续传开启后，会在上传过程中将上传位置写入本地磁盘文件，影响文件上传速度，请您根据实际情况选择是否开启*/
        //request.setEnableCheckpoint(false);
        /* OSS慢请求日志打印超时时间，是指每个分片上传时间超过该阈值时会打印debug日志，如果想屏蔽此日志，请调整该阈值。单位: 毫秒，默认为300000毫秒*/
        //request.setSlowRequestsThreshold(300000L);
        /* 可指定每个分片慢请求时打印日志的时间阈值，默认为300s*/
        //request.setSlowRequestsThreshold(300000L);
        /* 是否显示水印(可选)，指定模板组ID时，根据模板组配置确定是否显示水印*/
        //request.setIsShowWaterMark(true);
        /* 自定义消息回调设置(可选)，参数说明参考文档 https://help.aliyun.com/document_detail/86952.html#UserData */
        // request.setUserData("{\"Extend\":{\"test\":\"www\",\"localId\":\"xxxx\"},\"MessageCallback\":{\"CallbackURL\":\"http://test.test.com\"}}");
        /* 视频分类ID(可选) */
        //request.setCateId(0);
        /* 视频标签,多个用逗号分隔(可选) */
        //request.setTags("标签1,标签2");
        /* 视频描述(可选) */
        //request.setDescription("视频描述");
        /* 封面图片(可选) */
        //request.setCoverURL("http://cover.sample.com/sample.jpg");
        /* 模板组ID(可选) */
        //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56a33d");
        /* 工作流ID(可选) */
        //request.setWorkflowId("d4430d07361f0*be1339577859b0177b");
        /* 存储区域(可选) */
        //request.setStorageLocation("in-201703232118266-5sejdln9o.oss-cn-shanghai.aliyuncs.com");
        /* 开启默认上传进度回调 */
        //request.setPrintProgress(false);
        /* 设置自定义上传进度回调 (必须继承 VoDProgressListener) */
        //request.setProgressListener(new PutObjectProgressListener());
        /* 设置您实现的生成STS信息的接口实现类*/
        // request.setVoDRefreshSTSTokenListener(new RefreshSTSTokenImpl());
        /* 设置应用ID*/
        //request.setAppId("app-1000000");
        /* 点播服务接入点 */
        //request.setApiRegionId("cn-shanghai");
        /* ECS部署区域*/
        // request.setEcsRegionId("cn-shanghai");
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
    /**
     * 获取URL上传信息
     *
     * @param client 发送请求客户端
     * @return GetURLUploadInfosResponse 获取URL上传信息响应数据
     * @throws Exception
     */
    public static GetURLUploadInfosResponse getURLUploadInfos(DefaultAcsClient client) throws Exception {
        GetURLUploadInfosRequest request = new GetURLUploadInfosRequest();

        //上传源视频URL列表，需要URLencode
        String[] urls = {
                "http://exampleBucket****.cn-shanghai.aliyuncs.com/video_01.mp4",
                "http://exampleBucket****.cn-shanghai.aliyuncs.com/video_02.flv"
        };
        List<String> encodeUrlList = new ArrayList<String>();
        for (String url : urls) {
            encodeUrlList.add(URLEncoder.encode(url, "UTF-8"));
        }
        request.setUploadURLs(StringUtils.join(encodeUrlList, ','));
        //JobId列表，可以通过GetPlayInfo接口中返回的PlayInfo结构体中获取
        //request.setJobIds("exampleID1,exampleID2");

        return client.getAcsResponse(request);
    }



    public static void getPlayAuth() throws ClientException {
        //直接访问只能访问非解密视频，获取凭证访问能访问任何视频
        DefaultProfile profile;
        profile = DefaultProfile.getProfile("cn-shanghai", "LTAI5tFVf7ZKp9E522Jdhb9V", "fiOetyPaigHO1iuR2UATeX4PnDM2Zk");
        DefaultAcsClient client = new DefaultAcsClient(profile);
        //创建获取视频凭证的request和response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        //向request设置视频id
        request.setVideoId("90569d2dd24071eebf900675b3ed0102");
        //调用初始化对象的方法得到凭证
        response = client.getAcsResponse(request);
        System.out.println("playAuth:"+response.getPlayAuth());
    }

    //1 根据视频的id获取视频的播放地址
    public static void getPlayUrl(){
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
