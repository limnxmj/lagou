/**
 * FileName: RedisClientTemplate.java
 * Author:   limn_xmj@163.com
 * Date:     2020/8/3 7:18
 * Description:
 */
package cn.xmj.service;

import cn.xmj.config.JedisClusterConfig;
import cn.xmj.pojo.Position;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RedisClientTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClientTemplate.class);
    private static final String HOT_POSITION_KEY = "hot-position";

    @Autowired
    private Gson gson;

    @Autowired
    private JedisClusterConfig jedisClusterConfig;

    public boolean set(String key, Object value) {
        try {
            String str = jedisClusterConfig.getJedisCluster().set(key, String.valueOf(value));
            if ("OK".equals(str)) {
                return true;
            }
        } catch (Exception ex) {
            LOGGER.error("set:{key:{},value:{} error", key, value, ex);
        }
        return false;
    }

    public Object get(String key) {
        String str = null;
        try {
            str = jedisClusterConfig.getJedisCluster().get(key);
        } catch (Exception ex) {
            LOGGER.error("get:{key:{} error", key, ex);
        }
        return str;
    }


    public void lpushPosition(Position position) {
        try {
            jedisClusterConfig.getJedisCluster().lpush(HOT_POSITION_KEY, gson.toJson(position));
        } catch (Exception e) {
            LOGGER.error("lpushPosition:{key:{} error", HOT_POSITION_KEY, e);
        }
    }

    public List<Position> lrangePosition() {
        List<Position> positions = new ArrayList<>();
        try {
            List<String> lrange = jedisClusterConfig.getJedisCluster().lrange(HOT_POSITION_KEY, 0, -1);
            if (!CollectionUtils.isEmpty(lrange)) {
                for (String s : lrange) {
                    positions.add(gson.fromJson(s, Position.class));
                }
            }
        } catch (Exception e) {
            LOGGER.error("lrangePosition:{key:{} error", HOT_POSITION_KEY, e);
        }
        return positions;
    }

    public void ltrimPosition() {
        try {
            jedisClusterConfig.getJedisCluster().ltrim(HOT_POSITION_KEY, 1, 0);
        } catch (Exception e) {
            LOGGER.error("ltrimPosition:{key:{} error", HOT_POSITION_KEY, e);
        }
    }
}
