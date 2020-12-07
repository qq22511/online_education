package com.atguigu.ucenterservice.service.impl;

import com.atguigu.commonutils.utils.JwtUtils;
import com.atguigu.servicebase.handler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.ucenterservice.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-30
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void register(RegisterVo registerVo) {

        //1、取出所有参数
        //2、验空
        //3、验证手机号是否重复
        //4、根据手机号从redis获取验证码进行验证
        //5、对密码进行加密
        //6、补全信息、添加数据库

        //1、取出所有参数
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        //2、验空
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(mobile) ||StringUtils.isEmpty(nickname) ||StringUtils.isEmpty(password)){
            throw new GuliException(20001,"账号信息输入错误，注册失败");
        }

        //3、验证手机号是否重复
        QueryWrapper<UcenterMember> ucenterMemberWrapper = new QueryWrapper<>();
        ucenterMemberWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(ucenterMemberWrapper);
        if (count > 0){
            throw new GuliException(20001,"该手机号已注册");
        }

        //4、根据手机号从redis获取验证码进行验证
        String phoneCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(phoneCode)){
            throw new GuliException(20001,"验证码错误");
        }

        //5、对密码进行加密
        String passwordMD5 = MD5.encrypt(password);

        //6、补全信息，存入数据库
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(registerVo,ucenterMember);
        ucenterMember.setPassword(passwordMD5);
        ucenterMember.setIsDisabled(false);
        ucenterMember.setIsDeleted(false);
        ucenterMember.setAvatar("https://guli-file-190513.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        baseMapper.insert(ucenterMember);


    }

    @Override
    public String login(LoginVo loginVo) {
        //1、取出参数
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //2、验空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"用户名或密码错误");
        }

        //3、根据手机号取出用户信息，信息为空返回失败
        QueryWrapper<UcenterMember> memberWrapper = new QueryWrapper<>();
        memberWrapper.eq("mobile",mobile);
        UcenterMember member = baseMapper.selectOne(memberWrapper);
        if (member == null){
            throw new GuliException(20001,"用户名或密码有误");
        }

        //4、加密
        String passwordMD5 = MD5.encrypt(password);
        if (!passwordMD5.equals(member.getPassword())){
            throw new GuliException(20001,"用户名或密码错误");
        }

        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());


        return token;
    }

    /**
     * 根据日期统计用户信息
     * @param day 日期
     * @return 人数
     */
    @Override
    public Integer countRegister(String day) {
        Integer count =  baseMapper.countRegister(day);
        return count;
    }

}
