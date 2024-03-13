package com.atguigu.educms.controller;

import com.atguigu.commonutils.utils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/educms/bannerfront")
public class BannerFrontController {
    @Autowired
    private CrmBannerService bannerService;

    //��ѯ����banner
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        System.out.println("getAllBanner调用");
        List<CrmBanner> list = bannerService.selectAllBanner();
        return R.ok().data("list",list);
    }
}
