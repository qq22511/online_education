package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-11-29 23:10
 * @description:
 */
@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R deleteVideo(String videoId) {
        return R.error().message("time out");
    }

    @Override
    public R deleteVideoList(List<String> videoIdList) {
        return R.error().message("time out");
    }
}
