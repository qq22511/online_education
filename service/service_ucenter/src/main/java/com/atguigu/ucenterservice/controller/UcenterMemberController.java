package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.commonutils.vo.UcenterMemberForPay;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-30
 */
@Api(description = "用户客户管理")
@RestController
@RequestMapping("/ucenterservice/member")
@CrossOrigin
public class UcenterMemberController {


    @Autowired
    private UcenterMemberService ucenterMemberService;

    @ApiOperation("用户注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        //注册用户
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation("用户登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){

        String token = ucenterMemberService.login(loginVo);
        return R.ok().data("token",token);
    }

    @ApiOperation("根据token获取用户信息")
    @GetMapping("getInfoByToken")
    public R getInfoByToken(HttpServletRequest request){
        String id = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        return R.ok().data("ucenterMember",ucenterMember);
    }

    @ApiOperation("根据id获取用户信息,远程调用")
    @GetMapping("getUcenterInfoOrder/{memberId}")
    public UcenterMemberForPay getUcenterInfoOrder(@PathVariable("memberId") String memberId){
        UcenterMember ucenterMember = ucenterMemberService.getById(memberId);
        UcenterMemberForPay ucenterMemberForPay = new UcenterMemberForPay();
        BeanUtils.copyProperties(ucenterMember,ucenterMemberForPay);
        return ucenterMemberForPay;
    }

    @ApiOperation("根据日期统计用户信息")
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable("day") String day){

        Integer count =  ucenterMemberService.countRegister(day);
        return R.ok().data("countRegister",count);
    }

}

