package com.atguigu.cmsservice.service;

import com.atguigu.cmsservice.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-29
 */
public interface BannerService extends IService<Banner> {

    List<Banner> selectList();

    void updateBanner(Banner banner);
}
