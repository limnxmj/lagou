package cn.xmj.service;

import cn.xmj.entity.Position;

import java.util.List;

public interface PositionService {

    public List<Position> list();

    public void add(Position position);

    public void update(Position position);

    public void delete(long id);
}
