package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 培根香场
 * @Date 2020/11/25  10:47
 * @Description No Description
 */
@Data
@ApiModel(value = "章节信息")
public class ChapterVo {
    private String id;
    private String title;
    private List<VideoVo> children = new ArrayList<>();

}
