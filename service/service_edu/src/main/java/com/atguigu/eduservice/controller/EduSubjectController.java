package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.vo.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-23
 */
@Api(description = "文件读写")
@CrossOrigin
@RestController
@RequestMapping("/eduservice/subject")
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    @ApiOperation("批量导入课程分类")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){

        eduSubjectService.addSubject(file,eduSubjectService);
        return  R.ok();
    }

    @ApiOperation("查询所有课程")
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        List<OneSubject> oneSubjectList = eduSubjectService.getAllSubject();
        return R.ok().data("allSubject",oneSubjectList);
    }
}

