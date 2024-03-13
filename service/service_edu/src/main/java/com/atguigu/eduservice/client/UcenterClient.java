package com.atguigu.eduservice.client;


import com.atguigu.commonutils.utils.R;
import com.atguigu.commonutils.vo.UcenterMemberFront;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "service-ucenter",fallback = UcenterClientImpl.class)
@Component
public interface UcenterClient {
    //定义调用的方法路径
    //根据token获取用户信息
    @GetMapping("/educenter/member/getMember")
    public UcenterMemberFront getMemberFront(@RequestParam String memberId);
}
