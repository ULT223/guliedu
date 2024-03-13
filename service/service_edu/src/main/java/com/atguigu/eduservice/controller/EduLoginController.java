package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("eduService/user")
@Api(description = "登录")
public class EduLoginController {
    //login
    @PostMapping("login")
    @ApiOperation(value = "登录")
    public R login(){

        return R.ok().data("token","admin");
    }

    //info
    @GetMapping("info")
    @ApiOperation("用户信息")
    public R info(){
        return R.ok().data("name","admin").data("roles","[admin]").data("avatar","https://api-storage.4ce.cn/v1/b055cca63184a98bb3b50c980a73ed36.jpg");
    }
}
