/**
 * FileName: GuavaCacheApplication.java
 * Author:   limn_xmj@163.com
 * Date:     2020/8/11 21:46
 * Description:
 */
package cn.xmj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.xmj.mapper")
@SpringBootApplication
public class GuavaCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuavaCacheApplication.class, args);
    }
}
