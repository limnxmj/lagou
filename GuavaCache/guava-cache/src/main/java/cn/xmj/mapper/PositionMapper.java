/**
 * FileName: PositionMapper.java
 * Author:   limn_xmj@163.com
 * Date:     2020/8/11 21:56
 * Description:
 */
package cn.xmj.mapper;

import cn.xmj.pojo.Position;

import java.util.List;

public interface PositionMapper {

    public Position findById(Long id);

    public List<Position> findPositions();
}
