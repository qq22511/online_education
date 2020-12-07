package com.atguigu.eduservice.api;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.vo.CourseWebVoForPay;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-12-02 15:21
 * @description: 前端课程相关的页面
 */
@Api(description = "课程前台展示")
@RestController
@RequestMapping("/eduservice/courseapi")
@CrossOrigin
public class CourseApiController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    @ApiOperation("前台分页查询课程列表")
    @PostMapping("getCourseApiPageVo/{current}/{limit}")
    public R getCourseApiPageVo(
            @ApiParam(name = "current", value = "当前页码", required = true)
            @PathVariable Long current,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "CourseQueryVo", value = "查询对象", required = false)
            @RequestBody
                    CourseQueryVo courseQueryVo) {

        Page<EduCourse> page = new Page<>(current, limit);
        Map<String, Object> map = courseService.getCourseApiPageVo(page, courseQueryVo);
        return R.ok().data(map);
    }

    /**
     * 需求分析
     * 1、课程信息+课程描述+讲师信息
     * 2、课程大纲信息
     */

    @ApiOperation("前台查询课程详情")
    @GetMapping("getCourseApiInfo/{courseId}")
    public R getCourseApiInfo(@PathVariable String courseId,
                              HttpServletRequest request) {

        /**
         * 需求分析
         * 1、课程信息+课程描述+讲师信息
         * 2、课程大纲信息
         */

        //1、课程信息+课程描述+讲师信息
        CourseWebVo courseWebVo = courseService.getCourseApiInfo(courseId);

        //2、课程大纲信息
        List<ChapterVo> chapterVoList = chapterService.getChapterVideoById(courseId);

        //3、课程是否购买
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println("memberId = " + memberId);
        boolean isPayCourse = orderClient.isPayCourse(courseId, memberId);

        return R.ok().data("courseWebVo", courseWebVo).data("chapterVoList", chapterVoList).data("isPayCourse", isPayCourse);
    }


    @ApiOperation("查询课程信息，远程调用")
    @GetMapping("getCourseInfoForOrder/{courseId}")
    public CourseWebVoForPay getCourseInfoForOrder(@PathVariable("courseId") String courseId) {

        //获取课程信息用于订单模块
        CourseWebVo courseWebVo = courseService.getCourseApiInfo(courseId);
        CourseWebVoForPay courseWebVoForPay = new CourseWebVoForPay();

        //将课程信息放入courseWebVoForPay
        BeanUtils.copyProperties(courseWebVo, courseWebVoForPay);

        return courseWebVoForPay;
    }


}
