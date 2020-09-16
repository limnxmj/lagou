package cn.xmj.service;

import cn.xmj.pojo.Order;

public interface OrderService {

    //生成订单信息
    public Order genOrderInfo(Long user_id, Long goods_id);

    //预占订单
    public int submitOrder(Order order);

    public Order findOrderByOrderId(String orderId);

    public int updateOrderStatus(String orderId, Integer orderStatus);
}
