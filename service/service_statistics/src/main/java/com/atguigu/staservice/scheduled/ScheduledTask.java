package com.atguigu.staservice.scheduled;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author： bacon、
 * @version： 1.0
 * @create： 2020-12-07 10:32
 * @description:
 */
@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService dailyService;

    @Scheduled(cron = "0 0 4 1/1 * ? ")
    public void task(){

        String date = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));

        dailyService.createStaDaily(date);
    }


    /*@Scheduled(cron = "0/5 * * * * ?")
    public void testTask(){
        System.out.println("-------ScheduledTask-----testTask");
    }*/

}
