package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-04
 */
@Api(tags = {"订单管理"})
@RequestMapping("/orderservice/order")
@RestController
@CrossOrigin
public class TOrderController {

    @Autowired
    private TOrderService orderService;

    @ApiOperation("根据课程id和用户id查询购买情况")
    @GetMapping("isPayCourse/{cid}/{mid}")
    public boolean isPayCourse(@PathVariable("cid") String cid,
                               @PathVariable("mid") String mid){
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",cid);
        wrapper.eq("member_id",mid);
        wrapper.eq("status",1);
        int count = orderService.count(wrapper);
        return count>0;
    }

    @ApiOperation("根据订单id和会员id生成订单信息")
    @GetMapping("createOrder/{courseId}")
    public R createOrder(HttpServletRequest request,
                         @PathVariable String courseId) {
        //获取用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        //根据id查询用户信息，并为其创建该课程的订单
        String orderNo = orderService.createOrder(courseId, memberId);

        return R.ok().data("orderNo", orderNo);
    }

    @ApiOperation("根据订单编号查询订单信息")
    @GetMapping("getOrderByNo/{orderNo}")
    public R getOrderByNo(@PathVariable String orderNo) {
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        TOrder order = orderService.getOne(queryWrapper);

        return R.ok().data("order", order);
    }



}

