/**
 * FileName: EmailApplication.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 9:31
 * Description:
 */
package cn.xmj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class, args);
    }

}
