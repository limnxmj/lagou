package cn.xmj.service.impl;

import cn.xmj.mapper.GoodsMapper;
import cn.xmj.pojo.Goods;
import cn.xmj.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Integer getGoodsCount(Long id) {
        Integer goods_count = goodsMapper.getGoodsCount(id);
        if (goods_count == null) {
            goods_count = 0;
        }
        return goods_count;
    }

    @Override
    public Goods findGoodsById(Long id) {
        return goodsMapper.findGoodsById(id);
    }

    @Override
    public List<Goods> findGoods() {
        return goodsMapper.findGoods();
    }

    @Override
    public int updateGoodsCount(Goods goods) {
        return goodsMapper.updateGoodsCount(goods);
    }

    @Override
    public String checkGoodsCount(Long user_id, Long goods_id) {

        //1.redis限制某商品某人1分钟内1次
        Boolean absent = stringRedisTemplate.opsForValue().setIfAbsent("user:" + user_id + " goods:" + goods_id, "1", 60, TimeUnit.SECONDS);
        if (absent == null || !absent) {
            return "该商品正在抢购中, 请稍后…";
        }

        //2.校验库存

        String key = "goods:" + goods_id;
        stringRedisTemplate.setEnableTransactionSupport(true);
        stringRedisTemplate.watch(key);

        String goods_count_str = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(goods_count_str)) {
            return "该商品抢购结束…";
        }

        Integer goods_count = Integer.parseInt(goods_count_str);
        if (goods_count <= 0) {
            return "该商品抢购结束…";
        }
        stringRedisTemplate.multi();
        Long decrement = stringRedisTemplate.opsForValue().decrement(key, 1);
        List<Object> exec = stringRedisTemplate.exec();
        if (exec != null && exec.size() > 0) {
            return "";
        }
        return "抢购失败，请稍后重试…";
    }
}
