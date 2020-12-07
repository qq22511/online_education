package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-23
 */
public interface EduSubjectService extends IService<EduSubject> {

    /**
     * 添加课程
     * @param file 传输过来的文件
     * @param eduSubjectService Easy Excel需要eduSubjectService
     */
    void addSubject(MultipartFile file,EduSubjectService eduSubjectService);

    List<OneSubject> getAllSubject();

}
