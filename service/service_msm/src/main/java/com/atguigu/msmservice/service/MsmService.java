package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {
    boolean send(Map<String, String> params, String phone);
}
