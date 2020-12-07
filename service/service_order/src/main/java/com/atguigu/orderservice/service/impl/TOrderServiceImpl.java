package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.vo.CourseWebVoForPay;
import com.atguigu.commonutils.vo.UcenterMemberForPay;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-12-04
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public String createOrder(String courseId, String memberId) {


        //1、根据工具类生成订单编号
        String orderNo = OrderNoUtil.getOrderNo();

        //2、根据课程Id获取课程信息
        CourseWebVoForPay courseInfoForOrder = eduClient.getCourseInfoForOrder(courseId);
        if (courseInfoForOrder == null){
            throw new GuliException(20001,"课程信息失效");
        }

        //3、根据用户Id获取用户信息
        UcenterMemberForPay ucenterInfoOrder = ucenterClient.getUcenterInfoOrder(memberId);
        if (ucenterInfoOrder == null){
            throw new GuliException(20001,"获取用户信息失败");
        }

        //4、补全信息后，订单信息存入数据库
        TOrder order = new TOrder();
        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoForOrder.getTitle());
        order.setCourseCover(courseInfoForOrder.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseInfoForOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterInfoOrder.getMobile());
        order.setNickname(ucenterInfoOrder.getNickname());
        //0：未支付  1:已支付
        order.setStatus(0);
        //1：微信  2：支付宝
        order.setPayType(1);
        //存入数据库
        baseMapper.insert(order);
        return orderNo;
    }
}
