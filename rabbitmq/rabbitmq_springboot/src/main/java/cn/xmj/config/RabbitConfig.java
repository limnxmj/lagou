package cn.xmj.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        Map<String, Object> props = new HashMap<>();
        // 对于该队列中的消息，设置都等待10s
        props.put("x-message-ttl", 10000);
        // 设置该队列所关联的死信交换器（当队列消息TTL到期后依然没有消费，则加入死信 队列）
        props.put("x-dead-letter-exchange", "ex.pay.dlx");
        // 设置该队列所关联的死信交换器的routingKey，如果没有特殊指定，使用原队列的 routingKey
        props.put("x-dead-letter-routing-key", "key.pay.dlx");

        Queue queue = new Queue("q.pay", true, false, false, props);
        return queue;
    }

    @Bean public Exchange exchange() {
        DirectExchange exchange = new DirectExchange("ex.pay", true, false, null);
        return exchange;
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("key.pay").noargs();
    }

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue queueDlx() {
        Queue queue = new Queue("q.pay.dlx", true, false, false);
        return queue;
    }

    /**
     * 死信交换器
     * @return
     */
    @Bean
    public Exchange exchangeDlx() {
        DirectExchange exchange = new DirectExchange("ex.pay.dlx", true, false, null);
        return exchange;
    }

    /**
     * 死信交换器绑定死信队列
     * @return
     */
    @Bean
    public Binding bindingDlx() {
        return BindingBuilder.bind(queueDlx()).to(exchangeDlx()).with("key.pay.dlx").noargs();
    }
}
