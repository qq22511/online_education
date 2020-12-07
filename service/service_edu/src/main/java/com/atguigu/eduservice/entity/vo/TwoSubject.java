package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 培根香场
 * @Date 2020/11/24
 * @Time 10:16
 * @Description No Description
 */
@Data
public class TwoSubject {

    @ApiModelProperty("二级课程类别ID")
    private String id;

    @ApiModelProperty("二级类别名称")
    private String title;

}

