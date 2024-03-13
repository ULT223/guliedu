package com.atguigu.commonutils.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UcenterMemberFront {

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别 1 女，2 男")
    private Integer sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户签名")
    private String sign;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "是否禁用 1（true）已禁用，  0（false）未禁用")
    private Boolean isDisabled;

}
