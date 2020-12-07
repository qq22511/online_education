package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-12-04
 */
public interface TPayLogService extends IService<TPayLog> {

    Map<String, Object> createNative(String orderNo);

    void updateOrderInfo(Map<String, String> map);

    Map<String, String> queryPayStatus(String orderNo);
}
