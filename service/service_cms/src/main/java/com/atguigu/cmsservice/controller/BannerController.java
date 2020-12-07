package com.atguigu.cmsservice.controller;


import com.atguigu.cmsservice.entity.Banner;
import com.atguigu.cmsservice.service.BannerService;
import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-29
 */
@RestController
@RequestMapping("/cmsservice/banner")
@CrossOrigin
@Api(description = "网站首页Banner列表")
public class BannerController {


    @Autowired
    private BannerService bannerService;

    @ApiOperation("查询所有banner数据")
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<Banner> list = bannerService.selectList();
        return R.ok().data("list",list);
    }
}

