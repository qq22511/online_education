package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.orderservice.service.TPayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-04
 */
@Api(tags = "支付管理")
@CrossOrigin
@RestController
@RequestMapping("/orderservice/paylog")
public class TPayLogController {

    @Autowired
    private TPayLogService payLogService;

    @ApiOperation("根据订单Id生成微信支付二维码")
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {

        Map<String, Object> map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    @ApiOperation("根据订单Id查询支付状态")
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {

        //1、调用service查询支付状态
        Map<String, String> map = payLogService.queryPayStatus(orderNo);

        //判断map是否为空
        if (map == null) {
            return R.error().message("支持出错");
        }
        if ("SUCCESS".equals(map.get("trade_state"))) {

            //支付成功，更新订单状态
            payLogService.updateOrderInfo(map);
            return R.ok();
        }
        return R.ok().code(25000).message("支付中");
    }

}

