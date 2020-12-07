package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-25
 */
@RestController
@RequestMapping("/eduservice/chapter")
@CrossOrigin
@Api(description = "课程章节管理")
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    @ApiOperation("根据课程id查询章节、小节信息")
    @GetMapping("/getChapterVideoById/{courseId}")
    public R getChapterVideoById(@PathVariable String courseId){
        List<ChapterVo> chapterVoList = eduChapterService.getChapterVideoById(courseId);
        return R.ok().data("chapterVoList",chapterVoList);
    }

    @ApiOperation("添加章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.save(eduChapter);
        return R.ok();
    }

    @ApiOperation("根据id删除章节")
    @GetMapping("delChapter/{id}")
    public R delChapter(@PathVariable String id){
        eduChapterService.removeById(id);
        return R.ok();
    }

    @ApiOperation("根据id查询章节")
    @GetMapping("getChapterById/{id}")
    public R getChapterById(@PathVariable String id){
        EduChapter eduChapter = eduChapterService.getById(id);
        return R.ok().data("eduChapter",eduChapter);
    }

    @ApiOperation("修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){

        eduChapterService.updateById(eduChapter);
        return R.ok();
    }
}

