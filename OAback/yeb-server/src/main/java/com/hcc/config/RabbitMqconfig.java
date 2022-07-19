package com.hcc.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hcc.pojo.MailConstants;
import com.hcc.pojo.MailLog;
import com.hcc.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqconfig {

    public static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqconfig.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;
    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);

        /**
         * 消息确认回调
         * data消息唯一标识
         * ack 确认结果
         * cause 失败原因
         */
        rabbitTemplate.setConfirmCallback((data, ack, cause) ->{
            String msgid = data.getId();
            if (ack){
            LOGGER.info("{}============>发送成功",msgid);
            mailLogService.update(new UpdateWrapper<MailLog>().set("status",1)
                    .eq("msgId",msgid));
            }else {
                LOGGER.error("{}============>发送失败",msgid);
            }


        } );
        /**
         *msg:消息主题
         * repCode: 响应码
         * repText:相应描述
         * exchage:交换机
         * rotingkey:路由键
         */
        rabbitTemplate.setReturnCallback((msg, repCode, repText,exchange, routingkey) ->{
            LOGGER.error("{}============>消息发送queque失败",msg.getBody());
        } );

        return rabbitTemplate;
    }





    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }
}
