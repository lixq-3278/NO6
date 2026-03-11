package com.cl.config;

import com.cl.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class NotificationScheduleConfig {

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    public void retryFailedNotifications() {
        System.out.println("开始执行失败通知重试任务...");
        notificationService.retryFailedNotifications();
    }
}
