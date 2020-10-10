package cn.xmj.service;

import cn.xmj.entity.Position;
import cn.xmj.mapper.PositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionMapper positionMapper;

    @Override
    public List<Position> list() {
        List<Position> positions = positionMapper.list();
        if (positions == null) {
            return new ArrayList<>();
        }
        return positions;
    }

    @Override
    public void add(Position position) {
        if (position == null) {
            return;
        }
        positionMapper.add(position);
    }

    @Override
    public void update(Position position) {
        if (position == null || position.getId() == null) {
            return;
        }
        positionMapper.update(position);
    }

    @Override
    public void delete(long id) {
        positionMapper.delete(id);
    }
}
