package com.atguigu.orderservice.client;

import com.atguigu.commonutils.vo.UcenterMemberForPay;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-12-04 16:15
 * @description:
 */
@FeignClient("service-ucenter")
@Component
public interface UcenterClient {

    @ApiOperation("根据id获取用户信息,远程调用")
    @GetMapping("/ucenterservice/member/getUcenterInfoOrder/{memberId}")
    UcenterMemberForPay getUcenterInfoOrder(@PathVariable("memberId") String memberId);
}
