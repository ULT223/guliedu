package com.atguigu.eduorder.client;

import com.atguigu.commonutils.vo.UcenterMemberFront;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {
    //根据memberId获取用户信息
    @GetMapping("/educenter/member/getMember")
    public UcenterMemberFront getMemberFront(@RequestParam String memberId);
}
