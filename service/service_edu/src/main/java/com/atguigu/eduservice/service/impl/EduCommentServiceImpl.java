package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.mapper.EduCommentMapper;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2024-03-07
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    //分页查询评论
    @Override
    public Map<String, Object> getCommentPage(String courseId, Page<EduComment> commentPage) {
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.selectPage(commentPage,wrapper);
        List<EduComment> records = commentPage.getRecords();
        long current = commentPage.getCurrent();
        long pages = commentPage.getPages();
        long size = commentPage.getSize();
        long total = commentPage.getTotal();
        boolean hasNext = commentPage.hasNext();//下一页
        boolean hasPrevious = commentPage.hasPrevious();//上一页

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
}
