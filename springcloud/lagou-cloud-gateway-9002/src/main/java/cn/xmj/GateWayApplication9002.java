/**
 * FileName: GateWayApplication9002.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 9:40
 * Description:
 */
package cn.xmj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableDiscoveryClient
public class GateWayApplication9002 {

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication9002.class,args);
    }
}
