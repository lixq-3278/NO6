package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.YishengyuyueEntity;
import com.cl.service.JiuzhentongzhiService;
import com.cl.service.NotificationService;
import com.cl.service.TongzhijiluService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;

    @Autowired
    private TongzhijiluService tongzhijiluService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public void sendNotificationsOnAppointmentSuccess(YishengyuyueEntity appointment) {
        try {
            String[] reminderTypes = {"预约成功确认", "就诊前1天提醒", "就诊前2小时提醒"};
            
            for (String reminderType : reminderTypes) {
                JiuzhentongzhiEntity notification = createNotification(appointment, reminderType);
                jiuzhentongzhiService.insert(notification);
                
                boolean sent = sendNotification(notification);
                
                if (sent) {
                    notification.setZhuangtai("已发送");
                } else {
                    notification.setZhuangtai("发送失败");
                }
                jiuzhentongzhiService.updateById(notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JiuzhentongzhiEntity createNotification(YishengyuyueEntity appointment, String reminderType) {
        JiuzhentongzhiEntity notification = new JiuzhentongzhiEntity();
        notification.setTongzhibianhao(sdf.format(new Date()) + UUID.randomUUID().toString().substring(0, 8));
        notification.setYishengzhanghao(appointment.getYishengzhanghao());
        notification.setDianhua(appointment.getDianhua());
        notification.setJiuzhenshijian(appointment.getYuyueshijian());
        notification.setTongzhishijian(new Date());
        notification.setZhanghao(appointment.getZhanghao());
        notification.setShouji(appointment.getShouji());
        notification.setTongzhibeizhu(reminderType);
        notification.setAddtime(new Date());
        return notification;
    }

    @Override
    public boolean sendNotification(JiuzhentongzhiEntity notification) {
        TongzhijiluEntity record = new TongzhijiluEntity();
        record.setTongzhiid(notification.getId());
        record.setTongzhibianhao(notification.getTongzhibianhao());
        record.setZhanghao(notification.getZhanghao());
        record.setShouji(notification.getShouji());
        record.setTongzhineirong(notification.getTongzhibeizhu());
        record.setChongshicishu(0);
        record.setAddtime(new Date());
        record.setZuijinfasongshijian(new Date());

        try {
            boolean success = doSendNotification(notification);
            
            if (success) {
                record.setZhuangtai("发送成功");
                tongzhijiluService.insert(record);
                return true;
            } else {
                record.setZhuangtai("发送失败");
                record.setCuowuxinxi("发送失败，未知原因");
                tongzhijiluService.insert(record);
                return false;
            }
        } catch (Exception e) {
            record.setZhuangtai("发送失败");
            record.setCuowuxinxi(e.getMessage());
            tongzhijiluService.insert(record);
            return false;
        }
    }

    private boolean doSendNotification(JiuzhentongzhiEntity notification) {
        System.out.println("正在发送通知给用户: " + notification.getZhanghao() + 
                          ", 手机号: " + notification.getShouji() + 
                          ", 内容: " + notification.getTongzhibeizhu());
        return true;
    }

    @Override
    public void retryFailedNotifications() {
        EntityWrapper<TongzhijiluEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("zhuangtai", "发送失败")
               .lt("chongshicishu", 5);
        
        List<TongzhijiluEntity> failedRecords = tongzhijiluService.selectList(wrapper);
        
        for (TongzhijiluEntity record : failedRecords) {
            try {
                JiuzhentongzhiEntity notification = jiuzhentongzhiService.selectById(record.getTongzhiid());
                if (notification != null) {
                    boolean success = doSendNotification(notification);
                    record.setChongshicishu(record.getChongshicishu() + 1);
                    record.setZuijinfasongshijian(new Date());
                    
                    if (success) {
                        record.setZhuangtai("发送成功");
                        record.setCuowuxinxi("");
                        notification.setZhuangtai("已发送");
                        jiuzhentongzhiService.updateById(notification);
                    } else {
                        if (record.getChongshicishu() >= 5) {
                            record.setZhuangtai("重试失败");
                            record.setCuowuxinxi("已达到最大重试次数");
                        }
                    }
                    tongzhijiluService.updateById(record);
                }
            } catch (Exception e) {
                record.setChongshicishu(record.getChongshicishu() + 1);
                record.setZuijinfasongshijian(new Date());
                if (record.getChongshicishu() >= 5) {
                    record.setZhuangtai("重试失败");
                    record.setCuowuxinxi("已达到最大重试次数: " + e.getMessage());
                }
                tongzhijiluService.updateById(record);
            }
        }
    }
}
