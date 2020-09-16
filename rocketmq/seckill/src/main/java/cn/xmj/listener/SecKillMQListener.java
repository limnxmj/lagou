package cn.xmj.listener;

import cn.xmj.pojo.Goods;
import cn.xmj.pojo.Order;
import cn.xmj.service.GoodsService;
import cn.xmj.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RocketMQMessageListener(topic = "tp_seckill", consumerGroup = "consumer_seckill_grp_01")
public class SecKillMQListener implements RocketMQListener<Order> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillMQListener.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(Order order) {

        LOGGER.info("order:{}", order);

        Goods goods = goodsService.findGoodsById(order.getGoodsId());
        if (goods.getGoodsCount() <= 0) {
            return;
        }
        orderService.submitOrder(order);
        goods.setGoodsCount(goods.getGoodsCount() - 1);
        goodsService.updateGoodsCount(goods);

        //测试 30秒内支付超时
        stringRedisTemplate.opsForValue().set("order:" + order.getOrderId(), "1", 30, TimeUnit.SECONDS);

    }
}
