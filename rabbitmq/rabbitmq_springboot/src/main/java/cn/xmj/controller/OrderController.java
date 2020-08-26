package cn.xmj.controller;

import cn.xmj.container.OrderPayContainer;
import cn.xmj.dto.OrderPayDto;
import cn.xmj.id.IdWorker;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private IdWorker idWorker;

    @RequestMapping("/buy/{goods_name}/{goods_count}/{unit_price}")
    public String buy(Model mv,
                      @PathVariable(value = "goods_name") String goods_name,
                      @PathVariable(value = "goods_count") Integer goods_count,
                      @PathVariable(value = "unit_price") BigDecimal unit_price) {

        //生成订单号并存储到map（简化定义的存储待付款订单的容器）中
        String orderId = String.valueOf(idWorker.nextId());
        OrderPayDto orderPayDto = new OrderPayDto();
        orderPayDto.setOrderId(orderId);
        orderPayDto.setGoods_name(goods_name);
        orderPayDto.setGoods_count(goods_count);
        orderPayDto.setUnit_price(unit_price);
        BigDecimal total_price = new BigDecimal(goods_count).multiply(unit_price).setScale(2);
        orderPayDto.setTotal_price(total_price);
        OrderPayContainer.getOrderPayMap().put(orderId, orderPayDto);

        //发送消息到rabbitmq
        rabbitTemplate.convertAndSend("ex.pay", "key.pay", orderId);

        //返回
        String message = "商品名称：" + goods_name
                + "<br/>商品数量：" + goods_count
                + "<br/>商品单价：" + unit_price
                + "<br/>应付金额：" + total_price;
        mv.addAttribute("message", message);
        mv.addAttribute("orderId", orderId);
        return "order/pay";
    }

    @RequestMapping("/pay/{orderId}")
    public String pay(Model mv, @PathVariable(value = "orderId") String orderId) {
        OrderPayDto orderPayDto = OrderPayContainer.getOrderPayMap().get(orderId);
        if (orderPayDto == null) {
            mv.addAttribute("error", "订单" + orderId + "不存在");
            return "order/pay_error";
        }
        String payStatus = orderPayDto.getPayStatus();
        if ("1".equals(payStatus)) {
            mv.addAttribute("timeoutOrderPayMap", OrderPayContainer.getTimeoutOrderPayMap());
            return "order/pay_timeout_list";
        }
        //支付处理
        orderPayDto.setPayStatus("2");
        //返回支付成功页面
        return "order/success";
    }
}
