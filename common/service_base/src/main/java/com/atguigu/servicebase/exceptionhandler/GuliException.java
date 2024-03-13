package com.atguigu.servicebase.exceptionhandler;

import com.atguigu.commonutils.utils.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@AllArgsConstructor //生成有参数构造
@NoArgsConstructor //生成无参构造
@Slf4j
public class GuliException extends RuntimeException{

    private Integer code;//状态码

    private String msg;//异常消息

    //自定义异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(ArithmeticException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(getCode()).message(getMsg());
    }
}
