package com.atguigu.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.atguigu.service.OssService;
import com.atguigu.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        //Access_key名称 密码
        String access_key = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String access_key_pwd = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。

//        String objectName = "exampledir/exampleobject.txt";
//        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
//        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
//        String filePath= "D:\\localpath\\examplefile.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint,access_key,access_key_pwd);

        //获取文件名称
        String fileName= file.getOriginalFilename();
        //给每个文件加上独一无二的uuid防止重名，去掉uuid的'-'
        String uuid = UUID.randomUUID().toString().replaceAll("-","");


        //2 把文件按日期进行分类
        //2019/11/12/01.jpg
        //获取当前日期
        String datePath = new DateTime().toString("yyyy/MM/dd");
        fileName = datePath + "/" + uuid + fileName;

        try {
            //创建文件输入流
            InputStream inputStream = file.getInputStream();



            // 创建PutObjectRequest对象。参数:1.bucket名称 2.上传到oss文件路径和文件名称 3.文件输入流
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,inputStream);
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传文件。
            PutObjectResult result = ossClient.putObject(putObjectRequest);

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        }
        catch (Exception e){

        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            // 地址格式
            // https://guliedu15.oss-cn-shanghai.aliyuncs.com/wp_totono_1_1600.jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        }
    }
}
