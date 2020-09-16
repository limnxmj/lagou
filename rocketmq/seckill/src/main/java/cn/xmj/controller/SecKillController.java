package cn.xmj.controller;

import cn.xmj.pojo.Goods;
import cn.xmj.pojo.Order;
import cn.xmj.service.GoodsService;
import cn.xmj.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SecKillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillController.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/index")
    public String index(Model model) {
        List<Goods> goods = goodsService.findGoods();
        model.addAttribute("goods", goods);
        return "seckill/index";
    }

    @RequestMapping("/buynow/{user_id}/{goods_id}")
    @ResponseBody
    public String buynow(@PathVariable("user_id") Long user_id, @PathVariable("goods_id") Long goods_id) {

        try {
            //校验库存
            String checkResult = goodsService.checkGoodsCount(user_id, goods_id);
            if (StringUtils.isNotBlank(checkResult)) {
                return checkResult;
            }

            //生成订单信息，存储到rocketmq中
            Order order = orderService.genOrderInfo(user_id, goods_id);
            rocketMQTemplate.convertAndSend("tp_seckill", order);

        } catch (Exception e) {
            LOGGER.info("抢购失败", e);
            return "抢购失败，请稍后重试…";
        }
        return "抢购成功，请在30分钟内支付…";
    }
}
