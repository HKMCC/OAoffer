package com.hcc.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hcc.pojo.Employee;
import com.hcc.pojo.MailConstants;
import com.hcc.pojo.MailLog;
import com.hcc.service.IEmployeeService;
import com.hcc.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件定时发送
 */
public class MailTask {
    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 邮件发送定时任务
     * 10秒执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask(){
        //获取发送状态为0的邮件信息
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>()
                .eq("status", 0).lt("tryTime", LocalDateTime.now()));

        list.forEach(mailLog -> {
            //如果重试次数大于等于3，更新状态为投递失败不在重试
            if (mailLog.getCount()>=3){
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",2)
                .eq("msgId",mailLog.getMsgId()));
            }
            mailLogService.update(new UpdateWrapper<MailLog>()
                    .set("count",mailLog.getCount()+1)
                    .set("updateTime",LocalDateTime.now())
                    .set("tryTime",LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                    .set("msgId",mailLog.getMsgId()));

            List<Employee> emp = employeeService.getEmployee(mailLog.getEid());
            //发送MQ
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_NAME,
                    emp,new CorrelationData(mailLog.getMsgId()));

        });
    }
}
