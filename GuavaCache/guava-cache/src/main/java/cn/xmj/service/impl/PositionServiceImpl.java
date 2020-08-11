/**
 * FileName: PositionServiceImpl.java
 * Author:   limn_xmj@163.com
 * Date:     2020/8/11 22:01
 * Description:
 */
package cn.xmj.service.impl;

import cn.xmj.mapper.PositionMapper;
import cn.xmj.pojo.Position;
import cn.xmj.service.PositionService;
import cn.xmj.service.RedisClientTemplate;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class PositionServiceImpl implements PositionService {


    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private RedisClientTemplate redisClientTemplate;

    @Autowired
    private LoadingCache<String, Position> positionGuavaCache;

    @Override
    public Position findById(Long id) {
        return positionMapper.findById(id);
    }

    @Override
    public List<Position> findPositions() {
        List<Position> positionFromCache = findPositionFromCache();
        if (!CollectionUtils.isEmpty(positionFromCache)) {
            return positionFromCache;
        }
        return flushCache();
    }

    @Override
    public List<Position> findPositionFromCache() {

        List<Position> positions = findPositionFromGuavaCache();
        if (!CollectionUtils.isEmpty(positions)) {
            return positions;
        }
        return findPositionFromRedis();
    }

    private List<Position> findPositionFromGuavaCache() {
        Map<String, Position> positionMap = positionGuavaCache.asMap();
        List<Position> positions = new ArrayList<>();
        if (!CollectionUtils.isEmpty(positionMap)) {
            for (Map.Entry<String, Position> entry : positionMap.entrySet()) {
                positions.add(entry.getValue());
            }
        }
        Collections.sort(positions, new Comparator<Position>() {
            @Override
            public int compare(Position o1, Position o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
        return positions;
    }

    private void sortPositions(List<Position> positions) {
        if (CollectionUtils.isEmpty(positions)) {
            return;
        }
        Collections.sort(positions, new Comparator<Position>() {
            @Override
            public int compare(Position o1, Position o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
    }

    private List<Position> findPositionFromRedis() {
        List<Position> positions = redisClientTemplate.lrangePosition();
        sortPositions(positions);
        return positions;
    }

    /**
     * 从db中获取数据并刷到缓存
     */
    @Override
    public List<Position> flushCache() {
        List<Position> positions = positionMapper.findPositions();
        if (!CollectionUtils.isEmpty(positions)) {
            for (Position position : positions) {
                positionGuavaCache.put(String.valueOf(position.getId()), position);
                redisClientTemplate.lpushPosition(position);
            }
        }
        return positions;
    }

    @Override
    public void clearRedis() {
        redisClientTemplate.ltrimPosition();
    }

    @Override
    public void clearGuava() {
        positionGuavaCache.invalidateAll();
    }


}
