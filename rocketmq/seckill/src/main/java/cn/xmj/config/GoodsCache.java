package cn.xmj.config;

import cn.xmj.pojo.Goods;
import cn.xmj.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class GoodsCache implements ApplicationRunner {

    @Autowired
    private GoodsService goodsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsCache.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Goods> goods = goodsService.findGoods();
        if (CollectionUtils.isEmpty(goods)) {
            return;
        }
        for (Goods good : goods) {
            stringRedisTemplate.opsForValue().set("goods:" + good.getId(), String.valueOf(good.getGoodsCount()));
        }

        LOGGER.info("redis初始化库存结束, goods:{}", goods);
    }
}
