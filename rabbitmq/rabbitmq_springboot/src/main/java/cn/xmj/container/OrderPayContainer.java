package cn.xmj.container;

import cn.xmj.dto.OrderPayDto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderPayContainer {

    //需要支付的订单
    private static final Map<String, OrderPayDto> orderPayMap = new ConcurrentHashMap<>();

    //支付超时的订单
    private static final Map<String, OrderPayDto> timeoutOrderPayMap = new ConcurrentHashMap<>();

    public static Map<String, OrderPayDto> getOrderPayMap() {
        return orderPayMap;
    }

    public static Map<String, OrderPayDto> getTimeoutOrderPayMap() {
        return timeoutOrderPayMap;
    }
}
