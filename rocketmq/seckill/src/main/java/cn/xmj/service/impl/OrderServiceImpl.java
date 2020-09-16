package cn.xmj.service.impl;

import cn.xmj.id.IdWorker;
import cn.xmj.mapper.OrderMapper;
import cn.xmj.pojo.Order;
import cn.xmj.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order genOrderInfo(Long user_id, Long goods_id) {
        Order order = new Order();
        order.setOrderId(String.valueOf(idWorker.nextId()));
        order.setUserId(user_id);
        order.setGoodsId(goods_id);
        order.setOrderStatus(0);
        return order;
    }

    @Override
    public int submitOrder(Order order) {
        return orderMapper.saveOrder(order);
    }

    @Override
    public Order findOrderByOrderId(String orderId) {
        return orderMapper.findOrderByOrderId(orderId);
    }

    @Override
    public int updateOrderStatus(String orderId, Integer orderStatus) {
        return orderMapper.updateOrderStatus(orderId, orderStatus);
    }
}
