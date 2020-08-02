/**
 * FileName: RedisClientTemplate.java
 * Author:   limn_xmj@163.com
 * Date:     2020/8/3 7:18
 * Description:
 */
package cn.xmj.service;

import cn.xmj.config.JedisClusterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisClientTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClientTemplate.class);

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
}
