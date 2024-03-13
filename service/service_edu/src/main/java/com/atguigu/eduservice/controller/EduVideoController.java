package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.utils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2024-02-15
 */
@RestController
@RequestMapping("/eduservice/video")

public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    //注入vodClient
    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    //删除小节
    // TODO 需要完善:删除小节同时把视频删除
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        //根据小节id获取视频id
        EduVideo eduVideo = eduVideoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();

        //根据视频id，远程调用实现视频删除
        if (!StringUtils.isEmpty(videoSourceId)){
            R result = vodClient.removeAlyVideo(videoSourceId);
            if (result.getCode() == 20001)
                throw new GuliException(20001,"删除视频失败，熔断器...");

        }

        eduVideoService.removeById(id);
        return R.ok();
    }

    //修改小节 TODO
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

    @GetMapping("{videoId}")
    public R getVideoInfo(@PathVariable String videoId){
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("id",videoId);
        EduVideo video = eduVideoService.getOne(wrapper);
        return R.ok().data("video",video);
    }
}

