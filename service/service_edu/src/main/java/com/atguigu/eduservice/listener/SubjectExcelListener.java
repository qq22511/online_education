package com.atguigu.eduservice.listener;




import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * @author 培根香场
 * @Description 自己创建监听器去执行EasyExcel
 */

public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    private EduSubjectService subjectService;

    public SubjectExcelListener() {
    }

    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        //1读取数据验空
        if (excelSubjectData == null) {
            throw new GuliException(20001, "导入课程分类失败");
        }
        //2判断一级分类名称是否重复
        EduSubject existOneSubject = this.existOneSubject(excelSubjectData.getOneSubjectName(), subjectService);
        //3不重复插入数据库
        if (existOneSubject == null) {
            existOneSubject = new EduSubject();
            existOneSubject.setTitle(excelSubjectData.getOneSubjectName());
            existOneSubject.setParentId("0");
            subjectService.save(existOneSubject);
        }
        String pid = existOneSubject.getId();
        //4判断二级分类名称是否重复
        EduSubject existTwoSubject = this.existTwoSubject(excelSubjectData.getTwoSubjectName(), pid, subjectService);
        //5不重复插入数据库
        if (existTwoSubject == null) {
            existTwoSubject = new EduSubject();
            existTwoSubject.setTitle(excelSubjectData.getTwoSubjectName());
            existTwoSubject.setParentId(pid);
            subjectService.save(existTwoSubject);
        }

    }


    private EduSubject existOneSubject(String oneSubjectName, EduSubjectService subjectService) {

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", oneSubjectName);
        wrapper.eq("parent_id", "0");
        EduSubject one = subjectService.getOne(wrapper);
        return one;
    }

    private EduSubject existTwoSubject(String twoSubjectName, String pid, EduSubjectService subjectService) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", twoSubjectName);
        wrapper.eq("parent_id", pid);
        EduSubject two = subjectService.getOne(wrapper);
        return two;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
