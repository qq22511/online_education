package com.atguigu.voi.controller.admin;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.handler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.atguigu.voi.util.AliyunVodSDKUtils.initVodClient;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-11-27 19:50
 * @description: controller
 */
@Api(description = "视频管理")
@CrossOrigin
@RestController
@RequestMapping("/eduvod/video")
public class VideoAdminController {

    @ApiOperation("上传视频文件")
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file) {
        try {
            //准备上传文件所需的参数
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String title = file.getOriginalFilename()
                    .substring(0, fileName.lastIndexOf("."));

            //上传文件
            UploadStreamRequest request = new UploadStreamRequest(
                    "LTAI4GGAx4McMdcTxT38onN9",
                    "z2kL9pfxmOtSCoh6D12Ni2abuhONJi",
                    title,
                    fileName,
                    inputStream
            );
            UploadVideoImpl uploadVideo = new UploadVideoImpl();

            //上传之后收到响应
            UploadStreamResponse response = uploadVideo.uploadStream(request);


            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因

            String videoId = response.getVideoId();
            return R.ok().data("videoId", videoId);


        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(20001, "上传文件失败");
        }
    }


    @ApiOperation("删除视频文件")
    @DeleteMapping("deleteVideo/{videoId}")
    public R deleteVideo(@PathVariable("videoId") String videoId) {

        try {
            //1、创建初始化对象
            DefaultAcsClient client = initVodClient(
                    "LTAI4GGAx4McMdcTxT38onN9",
                    "z2kL9pfxmOtSCoh6D12Ni2abuhONJi");

            //2、创建请求
            DeleteVideoRequest request = new DeleteVideoRequest();

            /**
             * 3、向请求设置参数
             * 支持传入多个视频ID,使用多个，分割
             * 如("a,b")
             */
            request.setVideoIds(videoId);

            //4、用初始化对象调用方法发送请求，拿到响应，由于删除不需要返回数据
            client.getAcsResponse(request);

            return R.ok();
        } catch (ClientException e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除阿里云视频失败");
        }
    }

    @ApiOperation("批量删除视频")
    @DeleteMapping("deleteVideoList")
    public R deleteVideoList(@RequestParam("videoIdList") List<String> videoIdList) {

        try {
            //创建初始化对象
            DefaultAcsClient client = initVodClient(
                    "LTAI4GGAx4McMdcTxT38onN9",
                    "z2kL9pfxmOtSCoh6D12Ni2abuhONJi");

            //创建请求，响应对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            DeleteVideoResponse response = new DeleteVideoResponse();

            //向请求设置参数
            //支持传入多个ID，使用多个逗号进行分隔
            //如
            String vodeoIds = StringUtils.join(videoIdList.toArray(), ",");
            request.setVideoIds(vodeoIds);

            //4 用初始化对象调用方法发送请求，拿到响应
            response = client.getAcsResponse(request);

            //5 从响应中获取参数
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "批量删除文件失败");
        }
    }

    @ApiOperation("获取视频播放凭证视频")
    @GetMapping("getVideoAuthResponse/{videoSourceId}")
    public R getVideoAuthResponse(@PathVariable String videoSourceId) {

        //1、创建请求客户端
        DefaultAcsClient client = initVodClient(
                "LTAI4GGAx4McMdcTxT38onN9",
                "z2kL9pfxmOtSCoh6D12Ni2abuhONJi");

        //创建请求和响应
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {


            request.setVideoId(videoSourceId);
            response = client.getAcsResponse(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok().data("PlayAuth",response.getPlayAuth());
    }

}
