/**
 * FileName: ZkConfig.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/30 14:09
 * Description:
 */
package cn.xmj.config;

import cn.xmj.zookeeper.ZkClientUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkConfig {

    @Bean
    public ZkClientUtil zkClientUtil() {
        return new ZkClientUtil();
    }
}
