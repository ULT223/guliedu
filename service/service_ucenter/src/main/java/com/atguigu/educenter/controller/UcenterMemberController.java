package com.atguigu.educenter.controller;


import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.utils.R;
import com.atguigu.commonutils.vo.UcenterMemberFront;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2024-03-02
 */
@RestController
@RequestMapping("/educenter/member")
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService  memberService;

    //登录
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member){
        //调用service方法实现登录
        //返回token值，使用jwt生成
        String token = memberService.login(member);
        return R.ok().data("token",token);
    }

    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //调用jwt工具类的方法,根据reqeust对象获取头信息
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = memberService.getById(memberId);
        if(member == null)
            throw new GuliException(20001,"获取成员信息失败");
        return R.ok().data("userInfo",member);
    }

    //根据memberId获取用户信息
    @GetMapping("getMember")
    public UcenterMemberFront getMemberFront(@RequestParam String memberId){
        UcenterMember ucenterMember = memberService.getById(memberId);
        UcenterMemberFront member = new UcenterMemberFront();
        BeanUtils.copyProperties(ucenterMember,member);
        return member;
    }

    //查询某一天注册人数
    @GetMapping("countRegister/{day}")
    public R countRegisterDay(@PathVariable String day){
        Integer count = memberService.countRegisterDay(day);
        return R.ok().data("countRegister",count);
    }
}

