package com.atguigu.eduservice.entity.chapter;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "chapter查询返回对象", description = "chapter查询返回对象封装")
@Data
public class ChapterVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;

    //表示小节
    private List<VideoVo> children = new ArrayList<>();
}
