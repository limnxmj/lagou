package cn.xmj.listener;

import cn.xmj.container.OrderPayContainer;
import cn.xmj.dto.OrderPayDto;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderPayDlxListener {

    @RabbitListener(queues = "q.pay.dlx", ackMode = "MANUAL")
    public void doMessage(Message message, Channel channel) throws IOException {

        //死信消息处理 更改状态为1超时，并放入超时map
        String orderId = new String(message.getBody());
        OrderPayDto orderPayDto = OrderPayContainer.getOrderPayMap().get(orderId);
        if (orderPayDto == null) {
            return;
        }

        //对于未付款状态且已超时的订单 进行处理
        if ("0".equals(orderPayDto.getPayStatus())) {
            orderPayDto.setPayStatus("1");
            OrderPayContainer.getTimeoutOrderPayMap().put(orderId, orderPayDto);
        }

        //死信队列消费确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }
}
