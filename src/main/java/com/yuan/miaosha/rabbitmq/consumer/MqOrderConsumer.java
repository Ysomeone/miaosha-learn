package com.yuan.miaosha.rabbitmq.consumer;

import com.yuan.miaosha.entity.MqOrder;
import com.yuan.miaosha.service.MqOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author yuan
 * @Date 2020/5/24 14:38
 * @Version 1.0
 */
@Component
@Log4j2
public class MqOrderConsumer {

    @Resource
    private MqOrderService mqOrderService;

    /**
     * 用户下单支付超时-处理服务实例
     *
     * @param
     */
//    @RabbitListener(queues = "mq.consumer.order.real.queue.name", containerFactory = "singleListenerContainer")
    @RabbitListener(queues = "mq.queue.name")
    public void consumerMsg(@Payload String message){
        long orderId = Long.parseLong(message);
        log.info("用户下单支付超时-取消订单-取消订单id：orderId={}",orderId);
        MqOrder mqOrder = mqOrderService.find(orderId);
        if(mqOrder!=null && mqOrder.getStatus()==1){
            mqOrder.setStatus(3);
            mqOrderService.update(mqOrder);
        }
    }


}
