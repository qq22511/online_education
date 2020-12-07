package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-11-27 21:15
 * @description: 所有课程信息
 */
@Data
public class CourseQuery {

    @ApiModelProperty(value = "一级类别id")
    private String subjectParentId ;

    @ApiModelProperty(value = "二级类别id")
    private String subjectId ;

    @ApiModelProperty(value = "课程名称")
    private String title;

    @ApiModelProperty(value = "讲师id")
    private String teacherId;

}
