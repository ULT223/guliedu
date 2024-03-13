package com.atguigu.eduorder.client;

import com.atguigu.commonutils.vo.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-edu")
public interface EduClient {
    @PostMapping("/eduservice/coursefront/getCourseInfoOrder/{courseId}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable("courseId") String courseId);
}
