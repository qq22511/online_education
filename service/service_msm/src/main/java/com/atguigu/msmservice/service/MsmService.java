package com.atguigu.msmservice.service;

import java.util.Map;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-11-30 20:22
 * @description:
 */
public interface MsmService {
    boolean sendMsmPhone(String phone, Map<String, Object> param);
}
