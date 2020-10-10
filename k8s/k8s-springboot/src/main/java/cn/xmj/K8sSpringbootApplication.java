package cn.xmj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.xmj.mapper")
public class K8sSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8sSpringbootApplication.class, args);
    }

}
