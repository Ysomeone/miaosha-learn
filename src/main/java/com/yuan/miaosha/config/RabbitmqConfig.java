package com.yuan.miaosha.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * Rabbitmq 自定义注入Bean配置
 *
 * @Author yuan
 * @Date 2020/5/24 14:17
 * @Version 1.0
 */
@Configuration
@Log4j2
public class RabbitmqConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    @Autowired
    private Environment env;

    /**
     * 单一消费者
     *
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainerFactoryConfigurer() {
        SimpleRabbitListenerContainerFactory factoryConfigurer = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.setConnectionFactory(connectionFactory);
        factoryConfigurer.setMessageConverter(new Jackson2JsonMessageConverter());
        factoryConfigurer.setConcurrentConsumers(1);
        factoryConfigurer.setMaxConcurrentConsumers(1);
        factoryConfigurer.setPrefetchCount(1);
        return factoryConfigurer;
    }

    /**
     * 多个消费者
     *
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory, connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setConcurrentConsumers(10);
        factory.setMaxConcurrentConsumers(15);
        factory.setPrefetchCount(10);
        return factory;
    }


    @Bean
    public Queue orderQueue() {
        return new Queue("mq.queue.name", true);
    }

    @Bean
    public CustomExchange orderExchange() {
        Map<String, Object> pros = new HashMap<>();
        pros.put("x-delayed-type", "direct");
        return new CustomExchange("mq.exchange.name", "x-delayed-message", true, false, pros);
    }


    @Bean
    public Binding bindingNotify(@Qualifier("orderQueue") Queue queue,
                                 @Qualifier("orderExchange") CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with("mq.routing.key.name").noargs();
    }



    /**
     * 用户下单支付超时-RabbitMQ 死信队列消息模型构建
     */
    @Bean
    public Queue orderDeadQueue() {
        HashMap<String, Object> hashMap = new HashMap();
        hashMap.put("x-dead-letter-exchange", "mq.order.dead.exchange.name");
        hashMap.put("x-dead-letter-routing-key", "mq.order.dead.routing.key.name");
        /**
         * 设定TTL,单位为ms，在这里为了方便测试，设置为10s
         */
        hashMap.put("x-message-ttl", 10000);
        return new Queue("mq.order.dead.queue.name", true, false, false, hashMap);
    }

    /**
     * 创建“基本消息模型”的基本交换机 - 面向生产者
     *
     * @return
     */
    @Bean
    public TopicExchange orderProducerExchange() {
        return new TopicExchange("mq.producer.order.exchange.name", true, false);
    }

    /**
     * 创建“基本消息模型”的基本绑定-基本交换机+基本路由 - 面向生产者
     *
     * @return
     */
    @Bean
    public Binding orderProducerBinding() {
        return BindingBuilder.bind(orderDeadQueue()).to(orderProducerExchange()).with("mq.producer.order.routing.key.name");
    }


    /**
     * 创建真正队列 - 面向消费者
     *
     * @return
     */
    @Bean
    public Queue realOrderConsumerQueue() {
        return new Queue("mq.consumer.order.real.queue.name", true);
    }

    /**
     * 创建死信交换机
     *
     * @return
     */
    @Bean
    public TopicExchange basicOrderDeadExchange() {
        return new TopicExchange("mq.order.dead.exchange.name", true, false);
    }


    /**
     * 创建死信路由及其绑定
     *
     * @return
     */
    @Bean
    public Binding basicOrderDeadBinding() {
        return BindingBuilder.bind(realOrderConsumerQueue()).to(basicOrderDeadExchange()).with("mq.order.dead.routing.key.name");
    }

}
