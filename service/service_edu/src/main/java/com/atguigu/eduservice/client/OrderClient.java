package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-order")
public interface OrderClient {
    @GetMapping("/eduorder/order/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable(name = "courseId") String courseId, @PathVariable(name = "memberId") String memberId);
}
