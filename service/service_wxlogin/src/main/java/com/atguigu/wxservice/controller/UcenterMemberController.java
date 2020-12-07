package com.atguigu.wxservice.controller;


import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.wxservice.entity.UcenterMember;
import com.atguigu.wxservice.service.UcenterMemberService;
import com.atguigu.wxservice.utils.ConstantPropertiesUtil;
import com.atguigu.wxservice.utils.HttpClientUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-12-01
 */
@Api(description = "微信扫码登录")
@Controller
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    @ApiOperation(value = "微信扫码登录入口")
    @GetMapping("login")
    public String wxLogin() {
        //1拼写地址
        //方式一：https://open.weixin.qq.com/connect/qrconnect?
        // appid=APPID&redirect_uri=REDIRECT_URI
        // &response_type=code&scope=SCOPE&state=STATE
        //方式二：
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8"); //url编码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //拼接字符串
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                "wxatguigu");

        return "redirect:" + qrcodeUrl;
    }

    @ApiOperation(value = "微信扫码回调")
    @GetMapping("callback")//实现方法API
    public String  callback(String code, String state){
        System.out.println("code="+code);
        System.out.println("state="+state);

        //1向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        String accessTokenResult="";
        try {
            accessTokenResult=HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenResult="+accessTokenResult);
        } catch (Exception e) {
            throw new GuliException(20001, "获取access_token失败");
        }

        //2json串解析获取access_token，openid
        Gson gson = new Gson();
        HashMap<String,String> accessTokenMap = gson.fromJson(accessTokenResult, HashMap.class);
        String access_token = accessTokenMap.get("access_token");
        String openid = accessTokenMap.get("openid");

        //3 用access_token，openid访问微信换取用户信息
        String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
        String resultUserInfo = null;
        try {
            resultUserInfo = HttpClientUtils.get(userInfoUrl);
            System.out.println("resultUserInfo==========" + resultUserInfo);
        } catch (Exception e) {
            throw new GuliException(20001, "获取用户信息失败");
        }

        HashMap<String,String> userInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
        String nickname = userInfoMap.get("nickname");
        String headimgurl = userInfoMap.get("headimgurl");

        //4根据openid判断用户是否注册
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember ucenterMember = ucenterMemberService.getOne(wrapper);

        //5如果没有注册，注册用户
        if(ucenterMember==null){
            ucenterMember = new UcenterMember();
            ucenterMember.setOpenid(openid);
            ucenterMember.setNickname(nickname);
            ucenterMember.setAvatar(headimgurl);
            ucenterMember.setIsDisabled(false);
            ucenterMemberService.save(ucenterMember);
        }

        //6用户登录
        String token = JwtUtils.getJwtToken(ucenterMember.getId(),ucenterMember.getNickname());

        return "redirect:http://localhost:3000?token="+token;

        //http://localhost:3000/?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWxpLXVzZXIiLCJpYXQiOjE2MDY4ODQ4MTcsImV4cCI6MTYwNjk3MTIxNywiaWQiOiIxMzMzOTk2MjMzNTQyMzQwNjEwIiwibmlja25hbWUiOiLln7nmoLkifQ.jDujhFzP0oOYhvaxH9Oob2pwxKo2xlXxU3pEGv-saXI
    }

}