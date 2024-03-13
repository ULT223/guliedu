package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.utils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    //查询前8条热门课程，查询前4名老师
    @GetMapping("index")
    public R index(){
        //查询前8条热门课程
        List<EduCourse> eduCourseList = courseService.selectPopularCourse();

        //查询前4条名师
        List<EduTeacher> eduTeacherList = teacherService.selectPopularTeacher();
        return R.ok().data("eduCourseList",eduCourseList).data("eduTeacherList",eduTeacherList);
    }
}
