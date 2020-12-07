package com.atguigu.eduservice.api;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-11-30 14:57
 * @description:
 */
@Api(description = "首页展示")
@CrossOrigin
@RestController
@RequestMapping("/eduservice/index")
public class IndexController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation("查询首页8门课程，4位讲师")
    @GetMapping("getCourseTeacher")
    public R getCourseTeacher(){

        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByDesc("gmt_create");
        teacherWrapper.last("limit 4");
        List<EduTeacher> teacherList = eduTeacherService.list(teacherWrapper);

        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("gmt_create");
        courseWrapper.last("limit 8");
        List<EduCourse> courseList = eduCourseService.list(courseWrapper);
        return R.ok().data("teacherList",teacherList).data("courseList",courseList);
    }
}

