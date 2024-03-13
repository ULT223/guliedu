package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "课程查询对象封装",description = "课程查询对象封装")
@Data
public class CourseQuery {
    @ApiModelProperty("课程标题")
    private String title;

    @ApiModelProperty("课程状态(是否发布)")
    private String status;

    @ApiModelProperty(value = "查询开始时间", example = "2019-01-01 10:10:10")
    private String begin;//开始时间

    @ApiModelProperty(value = "查询结束时间", example = "2019-12-01 10:10:10")
    private String end;//截止时间
}
