package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-11-30 20:16
 * @description:
 */
@Api(description = "短信管理")
@CrossOrigin
@RestController
@RequestMapping("/edumsm/phone")
public class MsmApiController {


    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation("根据手机号发送验证码")
    @GetMapping("sendMsmPhone/{phone}")
    public R sendMsmPhone(@PathVariable String phone) {

        //1、从redis获取验证码
        String phoneCode = redisTemplate.opsForValue().get(phone);

        //2、如果有验证码，返回页面
        if (phoneCode != null) {
            return R.ok();
        }

        //3、如果没有，生成验证码
        String code = RandomUtil.getFourBitRandom();

        //4、调用service方法发送短信
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        boolean isSend = msmService.sendMsmPhone(phone,param);

        //5、发送成功后，验证码存入redis   5分钟有效
        if (isSend) {
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
        }

        return R.ok();
    }
}
