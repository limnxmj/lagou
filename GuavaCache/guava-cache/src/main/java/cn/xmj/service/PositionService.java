/**
 * FileName: PositionService.java
 * Author:   limn_xmj@163.com
 * Date:     2020/8/11 22:00
 * Description:
 */
package cn.xmj.service;

import cn.xmj.pojo.Position;

import java.util.List;

public interface PositionService {

    public Position findById(Long id);

    public List<Position> findPositions();

    public List<Position> findPositionFromCache();

    public List<Position> flushCache();

    public void clearRedis();

    public void clearGuava();
}
