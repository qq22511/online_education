package com.atguigu.orderservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.entity.TPayLog;
import com.atguigu.orderservice.mapper.TPayLogMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.service.TPayLogService;
import com.atguigu.orderservice.utils.HttpClient;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-12-04
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    private TOrderService orderService;


    /**
     * 根据订单Id生成微信支付二维码
     *
     * @param orderNo 订单ID
     * @return 数据
     */
    @Override
    public Map<String, Object> createNative(String orderNo) {


        try {
            //1、查询订单详情
            QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            TOrder order = orderService.getOne(wrapper);

            //2、判断订单是否超时
            if (order == null) {
                throw new GuliException(20001, "订单已超时");
            }
            //3、准备参数使用微信扫码
            Map m = new HashMap();
            //1、设置支付参数
            // 应用id
            m.put("appid", "wx74862e0dfcf69954");
            //商户号
            m.put("mch_id", "1558950191");
            //随机标号
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            //交易商品名称
            m.put("body", order.getCourseTitle());
            //在线教育系统的订单号
            m.put("out_trade_no", orderNo);
            //设置钱金额
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            //终端ip
            m.put("spbill_create_ip", "127.0.0.1");
            //回调地址
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            //交易类型
            m.put("trade_type", "NATIVE");

            //4、创建httpClient,设置固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //5、参数转化为xml
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //6、返回第三方的数据
            String xml = client.getContent();
            System.out.println("xml --->  " + xml);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //7、封装返回结果集合
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);

            return map;
        } catch (Exception e) {
            throw new GuliException(20001, "创建微信二维码失败");
        }

    }

    /**
     * 用户支付状态查询
     *
     * @param orderNo 订单编号
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //订单号
            m.put("out_trade_no", orderNo);

            //2、创建httpClient,设置固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");

            //3、带着参数请求，得到为xml
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //4、将xml转化为MAP
            String xml = client.getContent();
            System.out.println("xml --->  " + xml);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //返回数据
            return resultMap;
        } catch (Exception e) {
            throw new GuliException(20001, "查询微信支付状态失败");
        }

    }

    /**
     * 更新用户课程状态，改为已购买该课程
     *
     * @param map 从中获取订单id更新状态
     */
    @Override
    public void updateOrderInfo(Map<String, String> map) {

        //1、查询订单编号
        String orderNo = map.get("out_trade_no");

        //2、查询订单
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        TOrder order = orderService.getOne(wrapper);

        //3、更新课程购买状态
        order.setStatus(1);
        orderService.updateById(order);

        //4、记录支付日志
        TPayLog payLog = new TPayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));//微信返回的信息，留个底
        baseMapper.insert(payLog);//插入到支付日志表

    }

}
