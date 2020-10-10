package cn.xmj.mapper;

import cn.xmj.entity.Position;

import java.util.List;

public interface PositionMapper {

    public List<Position> list();

    public void add(Position position);

    public void update(Position position);

    public void delete(long id);

}
