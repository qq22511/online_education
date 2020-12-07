package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author 培根香场
 * @Date 2020/11/25  10:48
 * @Description No Description
 */
@Data
@ApiModel(value = "小节信息")
public class VideoVo {
    private String id;
    private String title;
    private Boolean free;
    private String videoSourceId;
}
