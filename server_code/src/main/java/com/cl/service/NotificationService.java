package com.cl.service;

import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.entity.YishengyuyueEntity;

public interface NotificationService {
    
    void sendNotificationsOnAppointmentSuccess(YishengyuyueEntity appointment);
    
    boolean sendNotification(JiuzhentongzhiEntity notification);
    
    void retryFailedNotifications();
}
