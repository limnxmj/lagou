package cn.xmj.mapper;

import cn.xmj.pojo.Goods;
import cn.xmj.pojo.Order;

import java.util.List;

public interface OrderMapper {

    public int saveOrder(Order order);

    public int updateOrderStatus(String orderId, Integer orderStatus);

    public Order findOrderByOrderId(String orderId);
}
