package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.utils.R;
import com.atguigu.commonutils.vo.CourseWebVoOrder;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.http.HttpRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;


    //1 条件查询
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo){

        System.out.println("courseFrontVo:"+courseFrontVo);
        Page<EduCourse> pageCourse = new Page<>(page,limit);
        Map<String,Object> map = courseService.getCourseFrontList(pageCourse,courseFrontVo);
        return R.ok().data(map);
    }

    //2 课程详情的方法
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){
        //根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
        //System.out.println("查询课程"+courseId+"前端详细");
        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);
        boolean buyCourse = false;
        //如果课程是收费的，根据课程id和用户id查询当前课程是否已经支付过了

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId))//若用户id为空
            return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);

        buyCourse = orderClient.isBuyCourse(courseId, memberId);
        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);
    }

    //3 根据课程id查询课程信息，返回课程对象
    @PostMapping("getCourseInfoOrder/{courseId}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String courseId){
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseWebVo,courseWebVoOrder);
        return courseWebVoOrder;
    }

}
