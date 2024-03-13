package com.atguigu.eduservice.client;

import com.atguigu.commonutils.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
@Component
public interface VodClient {
    //定义调用的方法路径
    @DeleteMapping("eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable String id);

    @DeleteMapping("eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoList);
}
