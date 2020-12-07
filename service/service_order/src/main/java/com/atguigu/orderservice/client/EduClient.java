package com.atguigu.orderservice.client;

import com.atguigu.commonutils.vo.CourseWebVoForPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-12-04 15:36
 * @description:
 */
@Component
@FeignClient("service-edu")
public interface EduClient {

    @GetMapping("/eduservice/courseapi/getCourseInfoForOrder/{courseId}")
    CourseWebVoForPay getCourseInfoForOrder(@PathVariable("courseId") String courseId);
}
