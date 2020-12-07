package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-12-05 15:38
 * @description:
 */
@FeignClient("service-order")
@Component
public interface OrderClient {

    @GetMapping("/orderservice/order/isPayCourse/{cid}/{mid}")
    public boolean isPayCourse(@PathVariable("cid") String cid,
                               @PathVariable("mid") String mid);



}
