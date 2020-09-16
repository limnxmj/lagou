package cn.xmj.mapper;

import cn.xmj.pojo.Goods;

import java.util.List;

public interface GoodsMapper {

    public Integer getGoodsCount(Long id);

    public Goods findGoodsById(Long id);

    public List<Goods> findGoods();

    public int updateGoodsCount(Goods goods);
}
