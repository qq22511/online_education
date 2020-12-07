package com.atguigu.eduservice.entity.vo;

import lombok.Data;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-11-26 14:56
 * @description: 课程发布前所有信息汇总
 */
@Data
public class CoursePublishVo {
    private String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;

    /** 只用于显示价格 */
    private String price;

}
