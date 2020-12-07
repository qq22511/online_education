package com.atguigu.staservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.staservice.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-05
 */
@Api(description = "统计分析")
@RestController
@CrossOrigin
@RequestMapping("/staservice/stadaily")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService dailyService;

    @ApiOperation("统计日期生成统计数据")
    @GetMapping("createStaDaily/{day}")
    public R createStaDaily(@PathVariable("day") String day){
        dailyService.createStaDaily(day);
        return R.ok();
    }

    @ApiOperation("根据参数查询统计数据")
    @GetMapping("getStaDaily/{type}/{begin}/{end}")
    public R getStaDaily(@PathVariable String type,
                         @PathVariable String begin,
                         @PathVariable String end){
        Map<String, Object> map = dailyService.getStaDaily(type,begin,end);
        return R.ok().data(map);
    }
}

