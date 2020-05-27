/**
 * FileName: LagouEurekaServerApp8761.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 8:29
 * Description:
 */
package cn.xmj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LagouEurekaServerApp8761 {

    public static void main(String[] args) {
        SpringApplication.run(LagouEurekaServerApp8761.class, args);
    }
}
