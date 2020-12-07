package com.atguigu.staservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-12-05 16:38
 * @description:
 */
@SpringBootApplication
@MapperScan({"com.atguigu.staservice.mapper"})
@ComponentScan("com.atguigu")
@EnableDiscoveryClient
@EnableFeignClients
//定时任务
@EnableScheduling
public class StaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StaApplication.class, args);
    }
}
