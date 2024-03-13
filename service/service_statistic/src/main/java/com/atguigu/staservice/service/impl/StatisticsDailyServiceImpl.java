package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.utils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author ULT
 * @since 2024-03-09
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void registerCount(String day) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);

        //远程调用得到某一天的注册人数
        R registerDay = ucenterClient.countRegisterDay(day);
        Integer countRegister = (Integer)registerDay.getData().get("countRegister");

        //把获取数据添加数据库，统计分析表里面
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister);//注册人数
        sta.setDateCalculated(day);//统计日期

        sta.setVideoViewNum(RandomUtils.nextInt(100,200));
        sta.setLoginNum(RandomUtils.nextInt(100,200));
        sta.setCourseNum(RandomUtils.nextInt(100,200));
        if (baseMapper.selectCount(wrapper) == 0){
            baseMapper.insert(sta);
        }else {
            baseMapper.update(sta,wrapper);
        }

    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
       //根据条件查询对应数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type);

        List<StatisticsDaily> statisticsDailies = baseMapper.selectList(wrapper);

        //因为返回有两部分数据，日期和日期对应数量
        //前端要求数组json结构
        //创建两个list集合，一个日期list，一个数量list
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();

        //遍历查询所有数据list集合，进行封装
        for (StatisticsDaily statisticsDaily : statisticsDailies) {
            //封装日期list集合
            date_calculatedList.add(statisticsDaily.getDateCalculated());
            //封装对应数量
            switch (type){
                case "login_num":
                    numDataList.add(statisticsDaily.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(statisticsDaily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(statisticsDaily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(statisticsDaily.getCourseNum());
                    break;
            }

        }
        //把封装之后的两个list集合放到map集合，进行返回
        Map<String,Object> map = new HashMap<>();
        map.put("date_calculatedList",date_calculatedList);
        map.put("numDataList",numDataList);
        return map;
    }
}
