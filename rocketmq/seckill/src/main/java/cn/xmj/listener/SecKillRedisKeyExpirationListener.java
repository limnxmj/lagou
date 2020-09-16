package cn.xmj.listener;

import cn.xmj.pojo.Goods;
import cn.xmj.pojo.Order;
import cn.xmj.service.GoodsService;
import cn.xmj.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class SecKillRedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillRedisKeyExpirationListener.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public SecKillRedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String expireKey = message.toString();

        LOGGER.info("expireKey:{}过期", expireKey);

        if (expireKey.startsWith("order:")) {

            String orderId = expireKey.split(":")[1];

            Order order = orderService.findOrderByOrderId(orderId);
            if (order == null) {
                return;
            }
            orderService.updateOrderStatus(orderId, 2);

            LOGGER.error("订单[{}]失效，库存还原+1", orderId);

            Goods goods = goodsService.findGoodsById(order.getGoodsId());
            goods.setGoodsCount(goods.getGoodsCount() + 1);
            goodsService.updateGoodsCount(goods);

            stringRedisTemplate.opsForValue().increment("goods:" + goods.getId(), 1);
        }
    }
}
