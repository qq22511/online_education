package com.atguigu.cmsservice.api;

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
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-11-29 23:39
 * @description:
 */
@RestController
@RequestMapping("/educms/banner")
@Api(description = "网站首页Banner列表")
@CrossOrigin //跨域
public class BannerApiController {

    @Autowired
    private BannerService bannerService;

    @ApiOperation("查询所有banner数据")
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<Banner> list = bannerService.list(null);
        return R.ok().data("list",list);
    }

}
