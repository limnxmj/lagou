package cn.xmj.service;

import cn.xmj.pojo.Goods;

import java.util.List;

public interface GoodsService {

    public Integer getGoodsCount(Long id);

    public Goods findGoodsById(Long id);

    public List<Goods> findGoods();

    public int updateGoodsCount(Goods goods);

    //检查库存
    public String checkGoodsCount(Long user_id, Long goods_id);
}
