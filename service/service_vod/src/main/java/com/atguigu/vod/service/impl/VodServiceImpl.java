package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitVodClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    @Override
    public String uploadVideo(MultipartFile file) {
        try {
            //accessKeyId,accessKeySecret
            //title 上传后显示名称
            //fileName: 上传文件原始名称
            //inputStream,上传文件输入流
            String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;
            String accessKeySecret = ConstantVodUtils.ACCESS_KEY_SECRET;
            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0,fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(accessKeyId,accessKeySecret,title,fileName,inputStream);

            /* 是否使用默认水印（可选），指定模板组ID时，根据模板组配置确定是否使用默认水印*/
            //request.setShowWaterMark(true);
            /* 自定义消息回调设置及上传加速设置（可选）, Extend为自定义扩展设置，MessageCallback为消息回调设置，AccelerateConfig为上传加速设置（上传加速功能需要先申请开通后才能使用）*/
            //request.setUserData("{\"Extend\":{\"test\":\"www\",\"localId\":\"xxxx\"},\"MessageCallback\":{\"CallbackType\":\"http\",\"CallbackURL\":\"http://example.aliyundoc.com\"},\"AccelerateConfig\":{\"Type\":\"oss\",\"Domain\":\"****Bucket.oss-accelerate.aliyuncs.com\"}}");
            /* 视频分类ID（可选） */
            //request.setCateId(0);
            /* 视频标签，多个用逗号分隔（可选） */
            //request.setTags("标签1,标签2");
            /* 视频描述（可选）*/
            //request.setDescription("视频描述");
            /* 封面图片（可选）*/
            //request.setCoverURL("http://cover.example.com/image_01.jpg");
            /* 模板组ID（可选）*/
            //request.setTemplateGroupId("8c4792cbc8694e7084fd5330e56****");
            /* 工作流ID（可选）*/
            //request.setWorkflowId("d4430d07361f0*be1339577859b0****");
            /* 存储区域（可选）*/
            //request.setStorageLocation("in-201703232118266-5sejd****.oss-cn-shanghai.aliyuncs.com");
            /* 开启默认上传进度回调 */
            // request.setPrintProgress(true);
            /* 设置自定义上传进度回调（必须继承 VoDProgressListener） */
            /*默认关闭。如果开启了这个功能，上传过程中服务端会在日志中返回上传详情。如果不需要接收此消息，需关闭此功能*/
            // request.setProgressListener(new PutObjectProgressListener());
            /* 设置应用ID*/
            //request.setAppId("app-100****");
            /* 点播服务接入点 */
            //request.setApiRegionId("cn-shanghai");
            /* ECS部署区域*/
            // request.setEcsRegionId("cn-shanghai");

            /* 配置代理访问（可选） */
            //OSSConfig ossConfig = new OSSConfig();
            /* <必填>设置代理服务器主机地址 */
            //ossConfig.setProxyHost("<yourProxyHost>");
            /* <必填>设置代理服务器端口 */
            //ossConfig.setProxyPort(-1);
            /* 设置连接OSS所使用的协议（HTTP或HTTPS），默认为HTTP */
            //ossConfig.setProtocol("HTTP");
            /* 设置用户代理，指HTTP的User-Agent头，默认为aliyun-sdk-java */
            //ossConfig.setUserAgent("<yourUserAgent>");
            /* 设置代理服务器验证的用户名，https协议时需要填 */
            //ossConfig.setProxyUsername("<yourProxyUserName>");
            /* 设置代理服务器验证的密码，https协议时需要填 */
            //ossConfig.setProxyPassword("<yourProxyPassword>");
            //request.setOssConfig(ossConfig);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
                System.out.print("VideoId=" + videoId + "\n");
            } else {
                /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
            return videoId;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void removeMoreAlyVideo(List<String> videoList) {
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //videoList转换成1,2,3
            String videoIds = StringUtils.join(videoList,",");
            //向request设置视频id
            request.setVideoIds(videoIds);
            client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }
}
