package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-11-28 11:55
 * @description: 远程调用VOD接口
 */
@Component
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
public interface VodClient {

    /**
     * 删除视频文件
     * 1、注解访问url必须完整
     * 2、参数注解的名字不能省略
     * @param videoId
     * @return
     */
    @DeleteMapping("/eduvod/video/deleteVideo/{videoId}")
    R deleteVideo(@PathVariable("videoId") String videoId);

    @DeleteMapping("/eduvod/video/deleteVideoList")
    R deleteVideoList(@RequestParam("videoIdList") List<String> videoIdList);
}
