package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2024-02-15
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //注入vodClient
    @Autowired
    private VodClient vodClient;

    //根据课程id删除小节
    //TODO 删除小节，删除对应视频文件
    @Override
    public void removeVideoByCourseId(String courseId) {
        //1 根据课程id查询课程所有的视频SourceId,删除课程同时删除阿里云视频
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        wrapperVideo.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);
        // 使用 Stream API 将 EduVideo 对象的 id 字段取出，并存储到一个 List<String> 中
        List<String> idList = eduVideoList.stream()
                .map(EduVideo::getVideoSourceId) // 使用 map 方法将每个 EduVideo 对象映射为其 id 字段的值
                .filter(videoSourceId->!videoSourceId.isEmpty())//过滤空值
                .collect(Collectors.toList()); // 使用 collect 方法将映射后的值收集到一个 List 中
        //删除阿里云中的视频
        if (!idList.isEmpty())//判断是否为空，为空则不需要删除
            vodClient.deleteBatch(idList);

        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
