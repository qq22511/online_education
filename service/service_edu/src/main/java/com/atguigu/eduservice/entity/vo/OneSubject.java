package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 培根香场
 * @Date 2020/11/24
 * @Time 10:17
 * @Description No Description
 */
@Data
public class OneSubject {

    @ApiModelProperty("课程Id")
    private String id;

    @ApiModelProperty("课程名称")
    private String title;

    @ApiModelProperty("2级课程")
    private List<TwoSubject> children = new ArrayList<>();
}
