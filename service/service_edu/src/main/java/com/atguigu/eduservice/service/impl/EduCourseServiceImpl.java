package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2024-02-15
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //课程描述
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    //注入小节和章节service
    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService eduChapterService;

    //添加课程信息
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1 向课程表添加课程基本信息
        //CourseInfoVo对象转换为eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);

        if (insert <= 0){
            //添加失败
            throw new GuliException(20001,"添加课程信息失败");
        }
        //获取添加之后课程id
        String cid = eduCourse.getId();

        //2 向课程简介表添加课程简介
        //edu_course_description
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述id就是课程id
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);
        return cid;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1 查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        //2 查询描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1 修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0){
            throw new GuliException(20001,"修改课程信息失败");
        }

        //2 修改描述表
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(eduCourseDescription);
    }

    //根据课程id查询课程确认信息
    @Override
    public CoursePublishVo getCoursePublishInfo(String courseId) {
        //调用mapper
        return baseMapper.getCoursePublishInfo(courseId);
    }

    @Override
    public void removeCourse(String courseId) {
        //1 根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);

        //2 根据课程id删除章节
        eduChapterService.removeChapterByCourseId(courseId);

        //3 根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        //4 根据课程id删除课程本身
        int result = baseMapper.deleteById(courseId);
        if (result == 0)
            throw new GuliException(20001,"删除失败");
    }

    @Override
    @Cacheable(key = "'course'",value = "popular")
    public List<EduCourse> selectPopularCourse() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduCoursePopularList = baseMapper.selectList(wrapper);
        return eduCoursePopularList;
    }

    //1 条件查询带分页查询课程
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse,CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if (courseFrontVo != null) {
            if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId()))
                wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
            if (!StringUtils.isEmpty(courseFrontVo.getSubjectId()))
                wrapper.eq("subject_id",courseFrontVo.getSubjectId());
            if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort()))//关注度
                wrapper.orderByDesc("buy_count");
            if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort()))//最新
                wrapper.orderByDesc("gmt_create");
            if (!StringUtils.isEmpty(courseFrontVo.getPriceSort()))//价格
                wrapper.orderByDesc("price");
        }
        baseMapper.selectPage(pageCourse, wrapper);
        //获取分页查询数据
        List<EduCourse> records = pageCourse.getRecords();
        long current = pageCourse.getCurrent();
        long pages = pageCourse.getPages();
        long size = pageCourse.getSize();
        long total = pageCourse.getTotal();
        boolean hasNext = pageCourse.hasNext();//下一页
        boolean hasPrevious = pageCourse.hasPrevious();//上一页

        //把分页数据取出来，放到map集合
        Map<String,Object> map = new HashMap<>();
        map.put("records",records);
        map.put("current",current);
        map.put("pages",pages);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);

        return map;
    }

    // 根据课程id，查询课程信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {

        return baseMapper.getBaseCourseInfo(courseId);
    }
}
