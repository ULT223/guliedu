package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.utils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
public class VodController {

    @Autowired
    private VodService vodService;

    //上传视频到阿里云
    @PostMapping("uploadAliyunVideo")
    public R uploadAliyunVideo(MultipartFile file){
        //返回上传视频的id值
        String videoId = vodService.uploadVideo(file);
        System.out.println("发送上传视频请求");
        return R.ok().data("videoSourceId",videoId);
    }

    //根据视频id删除阿里云视频
    @DeleteMapping("removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable String id){
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request设置视频id
            request.setVideoIds(id);
            client.getAcsResponse(request);
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }

    //删除多个阿里云视频的方法
    //参数多个视频id List videoList
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoList){
        vodService.removeMoreAlyVideo(videoList);
        return R.ok();
    }

    //根据视频id获取视频凭证
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id){
        System.out.println("videoId:"+id);
        try {
            //创建初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建获取凭证的request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

            //向request设置视频id
            request.setVideoId(id);

            //调用方法得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        }catch (Exception e){
            throw new GuliException(20001,"获取凭证失败");
        }
    }
}
