package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.atguigu.eduservice.entity.vo.OneSubject;
import com.atguigu.eduservice.entity.vo.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava==-=
 * @since 2020-11-23
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {


    @Override
    public void addSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            InputStream inputStream = file.getInputStream();

            EasyExcel.read(inputStream, ExcelSubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001, "导入课程失败");
        }

    }

    @Override
    public List<OneSubject> getAllSubject() {
        /**
         * 1、查询所有一级
         * 2、查询所有二级
         * 3、封装一级
         * 4、封装与一级相关的二级
         */
        //1、查询所有一级
        QueryWrapper<EduSubject> oneWrapper = new QueryWrapper<>();
        oneWrapper.eq("parent_id", "0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(oneWrapper);

        //2、查询所有二级
        QueryWrapper<EduSubject> twoWrapper = new QueryWrapper<>();
        twoWrapper.ne("parent_id", "0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(twoWrapper);

        //3、封装一级
        List<OneSubject> allSubjectList = new ArrayList<>();
        for (EduSubject subjectOne : oneSubjectList) {

            // 3.1 EduSubject转化为OneSubject
            OneSubject oneSubject = new OneSubject();

            //3.2 BeanUtils.copyProperties会对2个对象中相同的属性名进行复制
            BeanUtils.copyProperties(subjectOne, oneSubject);
            //3.3 添加
            allSubjectList.add(oneSubject);

            // 4 找到跟一级有关的二级进行封装
            List<TwoSubject> twoSubjects = new ArrayList<>();
            for (EduSubject subjectTwo : twoSubjectList) {

                //4.1 判断该二级分类是否属于该一类
                if (oneSubject.getId().equals(subjectTwo.getParentId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(subjectTwo,twoSubject);
                    twoSubjects.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoSubjects);
        }

        /**
         *  @Override
         *     public List<OneSubjectVo> getAllSubject() {
         *         //1查询所有一级分类
         *         QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
         *         wrapperOne.eq("parent_id","0");
         *         List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
         *         //2查询所有二级分类
         *         QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
         *         wrapperTwo.ne("parent_id","0");
         *         List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);
         *         //3封装一级分类
         *         List<OneSubjectVo> allSubjectList = new ArrayList<>();
         *         for (int i = 0; i < oneSubjectList.size(); i++) {
         *             //3.1取出每一个一级分类
         *             EduSubject oneSubject = oneSubjectList.get(i);
         *             //3.2 EduSubject转化成OneSubjectVo
         *             OneSubjectVo oneSubjectVo = new OneSubjectVo();
         * //            oneSubjectVo.setId(oneSubject.getId());
         * //            oneSubjectVo.setTitle(oneSubject.getTitle());
         *             BeanUtils.copyProperties(oneSubject,oneSubjectVo);
         *             allSubjectList.add(oneSubjectVo);
         *             //4找到跟一级有关的二级进行封装
         *             List<TwoSubjectVo> twoSubjectVos = new ArrayList<>();
         *             for (int m = 0; m < twoSubjectList.size() ; m++) {
         *                 //4.1取出每一个二级分类
         *                 EduSubject twoSubject = twoSubjectList.get(m);
         *                 //4.2判断是否归属此一级
         *                 if(twoSubject.getParentId().equals(oneSubject.getId())){
         *                     //3.2 EduSubject转化成TwoSubjectVo
         *                     TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
         *                     BeanUtils.copyProperties(twoSubject,twoSubjectVo);
         *                     twoSubjectVos.add(twoSubjectVo);
         *
         *                 }
         *             }
         *             oneSubjectVo.setChildren(twoSubjectVos);
         *         }
         *         return allSubjectList;
         *     }
         */
        //封装与一级相关的二级
        return allSubjectList;
    }
}
