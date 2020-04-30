package cn.xmj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.xmj.mapper")
@SpringBootApplication
public class ZkDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZkDatasourceApplication.class, args);
    }

}
