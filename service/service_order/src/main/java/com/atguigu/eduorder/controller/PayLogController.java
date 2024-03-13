package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.utils.R;
import com.atguigu.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author ULT
 * @since 2024-03-08
 */
@RestController
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    //生成微信支付二维码接口
    //参数:订单号
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){

        //返回信息，包含二维码地址，还有其他信息
        Map map = payLogService.createNative(orderNo);
        System.out.println("*****返回二维码map集合:"+map);
        return R.ok().data(map);
    }

    //查询订单支付状态
    //参数:订单号，根据订单号查询支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){

        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("****查询订单状态map集合:"+map);
        if (map == null){
            return R.error().message("支付出错了");
        }
        //如果返回map里不为空，通过map获取订单状态
        if (map.get("trade_state").equals("SUCCESS")){
            System.out.println("trade_state:"+map.get("trade_state"));
            //添加记录到支付表，更新订单表支付状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中");
    }
}

