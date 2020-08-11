/**
 * FileName: GuavaCacheConfig.java
 * Author:   limn_xmj@163.com
 * Date:     2020/8/11 22:42
 * Description:
 */
package cn.xmj.config;

import cn.xmj.mapper.PositionMapper;
import cn.xmj.pojo.Position;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Configuration
public class GuavaCacheConfig {

    @Autowired
    private PositionMapper positionMapper;

    @Bean(name = "positionGuavaCache")
    public LoadingCache<String, Position> loadingCache() {
        LoadingCache<String, Position> cache = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).build(
                new CacheLoader<>() {
                    @Override
                    public Position load(String key) throws Exception {
                        try {
                            return positionMapper.findById(Long.parseLong(key));
                        } catch (Exception e) {
                            return null;
                        }
                    }
                }

        );

        List<Position> positions = positionMapper.findPositions();
        if (!CollectionUtils.isEmpty(positions)) {
            for (Position position : positions) {
                cache.put(String.valueOf(position.getId()), position);
            }
        }
        return cache;
    }

}
