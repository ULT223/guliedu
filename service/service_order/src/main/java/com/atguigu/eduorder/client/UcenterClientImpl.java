package com.atguigu.eduorder.client;

import com.atguigu.commonutils.vo.UcenterMemberFront;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientImpl implements UcenterClient {

    @Override
    public UcenterMemberFront getMemberFront(String memberId) {
        throw  new GuliException(20001,"获取成员信息错误,请先登录");
    }
}
