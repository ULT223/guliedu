package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTask {

    @Autowired
    private StatisticsDailyService staService;


    //0/5 表示每隔5秒执行一次这个方法
    //@Scheduled(cron = "0/5 * * * * ?")
    public void task1(){
        System.out.println("*******task1执行了");
    }

    //每天凌晨一点执行方法，把数据查询进行添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {

        staService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }
}
