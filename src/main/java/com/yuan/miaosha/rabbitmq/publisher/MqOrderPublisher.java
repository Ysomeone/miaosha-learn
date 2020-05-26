package com.yuan.miaosha.rabbitmq.publisher;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Author yuan
 * @Date 2020/5/24 14:31
 * @Version 1.0
 */
@Component
@Log4j2
public class MqOrderPublisher {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(Long orderId){
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置基本交换机
//        rabbitTemplate.setExchange("mq.producer.order.exchange.name");
        //设置基本路由
//        rabbitTemplate.setRoutingKey("mq.producer.order.routing.key.name");


        rabbitTemplate.setExchange("mq.exchange.name");
        rabbitTemplate.setRoutingKey("mq.routing.key.name");


        rabbitTemplate.convertAndSend(orderId, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,Long.class);
                message.getMessageProperties().setDelay(6000);
                return message;
            }
        });
       log.info("用户下单支付超时-发送用户下单记录id的消息到死信队列：orderId={}",orderId);
    }

}
