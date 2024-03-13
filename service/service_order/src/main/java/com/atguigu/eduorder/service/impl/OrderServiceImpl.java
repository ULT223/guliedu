package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.vo.CourseWebVoOrder;
import com.atguigu.commonutils.vo.UcenterMemberFront;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author ULT
 * @since 2024-03-08
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    //1 生成订单的方法
    @Override
    public String createOrders(String courseId, String memberId) {
        System.out.println("courseId:"+courseId+",memberId:"+memberId);
        //通过远程调用获取用户信息
        UcenterMemberFront memberFront = ucenterClient.getMemberFront(memberId);

        //通过远程调用获取课程id信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        //创建订单
        Order order = new Order();
        String orderNo = IdWorker.getIdStr();//生成订单号
        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(memberFront.getMobile());
        order.setNickname(memberFront.getNickname());

        order.setStatus(0);//支付状态 :是否支付
        order.setPayType(1);//支付类型 1 微信 2 支付宝

        baseMapper.insert(order);

        //返回订单号
        return orderNo;
    }
}
