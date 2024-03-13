package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2024-01-20
 */
public interface EduTeacherService extends IService<EduTeacher> {

    List<EduTeacher> selectPopularTeacher();

    Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher);
}
