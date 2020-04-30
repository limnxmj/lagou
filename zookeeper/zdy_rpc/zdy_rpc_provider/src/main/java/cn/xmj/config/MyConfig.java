/**
 * FileName: MyConfig.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/22 10:53
 * Description:
 */
package cn.xmj.config;

import cn.xmj.server.MyServerBootstrap;
import com.lagou.zookeeper.ZkClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Value("${zdy.rpc.provider.host}")
    private String host;

    @Value("${zdy.rpc.provider.port}")
    private int port;

    @Bean
    public MyServerBootstrap myServerBootstrap() {
        return new MyServerBootstrap(host, port);
    }

    @Bean
    public ZkClientUtil zkClientUtil() {
        return new ZkClientUtil();
    }
}
