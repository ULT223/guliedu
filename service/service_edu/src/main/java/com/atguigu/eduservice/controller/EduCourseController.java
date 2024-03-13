package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.utils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2024-02-15
 */
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    //添加课程基本信息的方法
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    //根据课程id查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo= courseService.getCoursePublishInfo(id);
        return R.ok().data("CoursePublishInfo",coursePublishVo);
    }

    //课程最终发布
    //修改课程状态
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //课程列表 基本实现
    //TODO 完善条件查询带分页
    @GetMapping
    public R getCourseList(){
        List<EduCourse> list = courseService.list(null);
        return  R.ok().data("list",list);
    }

    @PostMapping("/{current}/{limit}")
    public R getCourseList(@PathVariable long current, @PathVariable long limit, @RequestBody CourseQuery courseQuery){
        Page<EduCourse> page = new Page<>(current,limit);
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        String begin = courseQuery.getBegin();
        String end = courseQuery.getEnd();
        if (!StringUtils.isEmpty(title)){
            wrapper.eq("title",courseQuery.getTitle());
        }
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("status",courseQuery.getStatus());
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",courseQuery.getBegin());
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",courseQuery.getEnd());
        }
        courseService.page(page, wrapper);
        long total = page.getTotal();
        List<EduCourse> records = page.getRecords();
        return R.ok().data("records",records).data("total",total);
    }

    @ApiOperation(value = "逻辑删除课程")
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }
}

