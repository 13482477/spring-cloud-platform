package com.siebre.config;

import com.siebre.payment.billing.amqp.RealTimeReconcileListener;
import com.siebre.payment.billing.amqp.RefundRealTimeReconcileListener;
import com.siebre.payment.paymentlistener.PaymentOrderOutOfTimeListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Huang Tianci
 * rabbitmq配置类
 */
@Configuration
public class RabbitmqConfig {

    public static String order_out_of_time_exchange = "orderOutOfTimeExchange";

    public static String order_out_of_time_queue = "orderOutOfTimeQueue";

    public static String order_out_of_time_key = order_out_of_time_queue;

    public static String order_real_time_recon_exchange = "orderRealTimeReconExchange";

    public static String order_real_time_recon_queue = "orderRealTimeReconQueue";

    public static String order_real_time_recon_key = order_real_time_recon_queue;

    public static String order_refund_real_time_recon_exchange = "orderRefundRealTimeReconExchange";

    public static String order_refund_real_time_recon_queue = "orderRefundRealTimeReconQueue";

    public static String order_refund_real_time_recon_key = order_refund_real_time_recon_queue;

    @Autowired
    private Environment environment;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(environment.getProperty("spring.rabbitmq.host"));
        connectionFactory.setUsername(environment.getProperty("spring.rabbitmq.username"));
        connectionFactory.setPassword(environment.getProperty("spring.rabbitmq.password"));
        connectionFactory.setPort(Integer.valueOf(environment.getProperty("spring.rabbitmq.port")));
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        return rabbitAdmin ;
    }

    /** 订单支付超时 exchange */
    @Bean
    public TopicExchange orderOutOfTimeExchange() {
        TopicExchange topicExchange = new TopicExchange(order_out_of_time_exchange);
        topicExchange.setDelayed(true);
        return topicExchange;
    }

    /** 订单实时对账 exchange */
    @Bean
    public TopicExchange orderRealTimeReconExchange() {
        TopicExchange topicExchange = new TopicExchange(order_real_time_recon_exchange);
        return topicExchange;
    }

    /** 订单退款实时对账 exchange */
    @Bean
    public TopicExchange orderRefundRealTimeReconExchange() {
        TopicExchange topicExchange = new TopicExchange(order_refund_real_time_recon_exchange);
        return topicExchange;
    }

    /** 订单支付超时 queue */
    @Bean
    public Queue orderOutOfTimeQueue() {
        return new Queue(order_out_of_time_queue);
    }

    /** 订单实时对账 queue */
    @Bean
    public Queue orderRealTimeReconQueue() {
        return new Queue(order_real_time_recon_queue);
    }

    /** 订单退款实时对账 queue */
    @Bean
    public Queue orderRefundRealTimeReconQueue() {
        return new Queue(order_refund_real_time_recon_queue);
    }

    /** 订单消息超时处理类 */
    @Bean
    public ChannelAwareMessageListener paymentOrderOutOfTimeListener() {
        return new PaymentOrderOutOfTimeListener();
    }

    @Bean
    public MessageListener realTimeReconcileListener() {
        return new RealTimeReconcileListener();
    }

    @Bean
    public MessageListener refundRealTimeReconcileListener() {
        return new RefundRealTimeReconcileListener();
    }

    @Bean
    public Binding outOfTimeBinding() {
        Binding binding = BindingBuilder.bind(orderOutOfTimeQueue()).to(orderOutOfTimeExchange()).with(order_out_of_time_key);
        return binding;
    }

    @Bean
    public Binding realTimeBinding() {
        Binding binding = BindingBuilder.bind(orderRealTimeReconQueue()).to(orderRealTimeReconExchange()).with(order_real_time_recon_key);
        return binding;
    }

    @Bean
    public Binding refundRealTimeBinding() {
        Binding binding = BindingBuilder.bind(orderRefundRealTimeReconQueue()).to(orderRefundRealTimeReconExchange()).with(order_refund_real_time_recon_key);
        return binding;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(order_out_of_time_queue);
        /*container.setQueueNames(order_real_time_recon_queue);
        container.setQueueNames(order_refund_real_time_recon_queue);*/
        container.setMessageListener(paymentOrderOutOfTimeListener());
        /*container.setMessageListener(realTimeReconcileListener());
        container.setMessageListener(refundRealTimeReconcileListener());*/
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer2() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(order_real_time_recon_queue);
        container.setMessageListener(realTimeReconcileListener());
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer3() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(order_refund_real_time_recon_queue);
        container.setMessageListener(refundRealTimeReconcileListener());
        return container;
    }

}
