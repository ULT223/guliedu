package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.utils.R;
import com.atguigu.commonutils.vo.UcenterMemberFront;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/commentfront")
public class CommentFrontController {

    @Autowired
    private EduCommentService eduCommentService;

    @Autowired
    private UcenterClient ucenterClient;

    // 根据课程id分页查询评论
    @ApiOperation(value = "分页查询评论")
    @GetMapping("/{page}/{limit}")
    public R PageListComment(@RequestParam String courseId, @PathVariable long page, @PathVariable long limit){
        Page<EduComment> commentPage = new Page<>(page,limit);
        //System.out.println(courseId);
        Map<String,Object> map = eduCommentService.getCommentPage(courseId,commentPage);
        return R.ok().data(map);
    }

    //添加评论
    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().code(28004).message("请先登录");
        }
        UcenterMemberFront memberFront = ucenterClient.getMemberFront(memberId);

        comment.setMemberId(memberId);
        comment.setAvatar(memberFront.getAvatar());
        comment.setNickname(memberFront.getNickname());
        eduCommentService.save(comment);

        return R.ok();
    }

}
