package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-25
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    @Override
    public void delVideo(String id) {
        //1根据id查询视频数据
        EduVideo eduVideo = baseMapper.selectById(id);
        String videoId = eduVideo.getVideoSourceId();
        //2根据videoId远程删除视频
        if(videoId!=null){
            vodClient.deleteVideo(videoId);
        }
        //3删除小节信息
        baseMapper.deleteById(id);

    }
}
