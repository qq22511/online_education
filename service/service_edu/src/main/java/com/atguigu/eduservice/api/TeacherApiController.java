package com.atguigu.eduservice.api;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-12-02 11:26
 * @description:
 */
@Api(description = "讲师前台展示")
@RestController
@RequestMapping("/eduservice/teacherapi")
@CrossOrigin
public class TeacherApiController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "前台分页查询讲师列表")
    @GetMapping("/getTeacherPage/{current}/{limit}")
    public R getTeacherPage(@PathVariable Long current,
                            @PathVariable Long limit) {

        Page<EduTeacher> page = new Page<>(current,limit);
        Map<String , Object> map = teacherService.getTeacherInfo(page);

        return R.ok().data(map);
    }

    @ApiOperation(value = "前台查询讲师详情")
    @GetMapping("getTeacherCourse/{teacherId}")
    public R getTeacherCourse(@PathVariable String teacherId){

        //1、根据id查询讲师信息
        EduTeacher eduTeacher = teacherService.getById(teacherId);

        //2、根据teacherId查询课程信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);
        return R.ok().data("eduTeacher",eduTeacher).data("courseList",courseList);
    }
}
