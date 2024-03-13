package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.utils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author ULT
 * @since 2024-03-08
 */
@RestController
@RequestMapping("/eduorder/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //1 生成订单方法
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId))
            return R.error().code(28004).message("请先登录");
        String orderNo = orderService.createOrders(courseId,memberId);//创建订单返回订单号
        return R.ok().data("orderNo",orderNo);
    }

    //2 根据订单id查询订单信息
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        Order order = orderService.getOne(wrapper);
        return  R.ok().data("order",order);
    }

    //根据课程id和用户id查询订单表中订单状态
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);//支付状态,1表示已支付
        int count = orderService.count(wrapper);
        if (count > 0)//已经支付
            return true;
        else
            return false;
    }

}

