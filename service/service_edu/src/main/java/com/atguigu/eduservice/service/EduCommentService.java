package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author testjava
 * @since 2024-03-07
 */
public interface EduCommentService extends IService<EduComment> {


    //分页查询课程评论
    Map<String, Object> getCommentPage(String courseId, Page<EduComment> commentPage);
}
