package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.*;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-24
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {


    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private VodClient vodClient;

    /**
     * 添加课程信息
     */
    @Override
    public String addCourseInfo(CourseInfoForm courseInfoForm) {

        //添加课程信息
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm, course);
        int insert = baseMapper.insert(course);

        //判断是否添加成功
        if (insert == 0) {
            throw new GuliException(20001, "添加课程失败");
        }

        //添加EduCourseDescription
        String id = course.getId();
        EduCourseDescription description = new EduCourseDescription();
        description.setId(id);
        description.setDescription(courseInfoForm.getDescription());
        eduCourseDescriptionService.save(description);
        return id;
    }

    /**
     * 根据ID查询课程信息
     */
    @Override
    public CourseInfoForm getCourseInfoById(String id) {

        /**
         * 1、根据id查询课程信息
         * 2、封装课程信息
         * 3、根据id查询课程描述
         * 4、封装课程描述
         */
        //1、根据id查询课程信息
        EduCourse eduCourse = baseMapper.selectById(id);

        //2、封装课程信息
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(eduCourse, courseInfoForm);

        //3、根据id查询课程描述
        EduCourseDescription description = eduCourseDescriptionService.getById(id);

        //4、封装课程描述
        courseInfoForm.setDescription(description.getDescription());
        return courseInfoForm;
    }

    //更新课程信息
    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm) {
        /**
         * 1、复制课程数据
         * 2、更新课程信息
         * 3、判断是否进行后续操作
         * 4、yes：更新描述信息
         */
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm, eduCourse);

        int insert = baseMapper.updateById(eduCourse);
        if (insert == 0) {
            throw new GuliException(20001, "更新失败");
        }
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoForm, eduCourseDescription);
        eduCourseDescriptionService.updateById(eduCourseDescription);
    }

    @Override
    public CoursePublishVo getCoursePublishById(String id) {

        return baseMapper.getCoursePublishById(id);
    }

    @Override
    public void delCourseInfo(String id) {

        /**
         * 0.删除视频
         * 1、删除小节
         * 2、删除章节
         * 3、删除课程描述
         * 4、删除课程
         */
        //0、删除视频
        //1、删除视频
        //1.1查询满足条件的小节
        QueryWrapper<EduVideo> videoIdWrapper = new QueryWrapper<>();
        videoIdWrapper.eq("course_id", id);
        List<EduVideo> videoList = eduVideoService.list(videoIdWrapper);
        //1.2判断是否为空
        if (videoList != null) {
            //1.3遍历封装
            List<String> videoIdList = new ArrayList<>();
            for (int i = 0; i < videoList.size(); i++) {
                EduVideo eduVideo = videoList.get(i);
                videoIdList.add(eduVideo.getVideoSourceId());
            }
            //1.4远程调用
            if (videoIdList.size() > 0) {
                vodClient.deleteVideoList(videoIdList);
            }
        }


        //1.删除小节
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", id);
        eduVideoService.remove(videoWrapper);

        //2、删除章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", id);
        eduChapterService.remove(chapterWrapper);

        //3、 删除课程描述
        eduCourseDescriptionService.removeById(id);

        //4、删除课程
        int i = baseMapper.deleteById(id);
        if (i == 0) {
            throw new GuliException(20001, "删除失败");
        }

    }

    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {

        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();

        if (courseQuery == null) {
            baseMapper.selectPage(pageParam, courseWrapper);
        }

        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();

        if (!StringUtils.isEmpty(title)) {
            courseWrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            courseWrapper.like("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            courseWrapper.like("subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            courseWrapper.like("subject_parent_id", subjectParentId);
        }

        baseMapper.selectPage(pageParam, courseWrapper);

    }

    //带条件分页查询课程列表
    @Override
    public Map<String, Object> getCourseApiPageVo(Page<EduCourse> pageParam, CourseQueryVo courseQueryVo) {

        //1、取出查询条件
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String subjectId = courseQueryVo.getSubjectId();
        String buyCountSort = courseQueryVo.getBuyCountSort();
        String gmtCreateSort = courseQueryVo.getGmtCreateSort();
        String priceSort = courseQueryVo.getPriceSort();

        //2、判断是否为空，不为空拼sql
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id", subjectId);
        }
        if (!StringUtils.isEmpty(buyCountSort)) {
            wrapper.orderByDesc("buy_count", buyCountSort);
        }
        if (!StringUtils.isEmpty(gmtCreateSort)) {
            wrapper.orderByDesc("gmt_create", gmtCreateSort);
        }
        if (!StringUtils.isEmpty(priceSort)) {
            wrapper.orderByDesc("price", priceSort);
        }

        //3、分页查询
        baseMapper.selectPage(pageParam, wrapper);

        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        //4、查询结构封装map
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;

    }

    //前台查询课程详情
    @Override
    public CourseWebVo getCourseApiInfo(String courseId) {
        CourseWebVo courseWebVo = baseMapper.getCourseApiInfo(courseId);
        return courseWebVo;
    }
}
