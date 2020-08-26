package cn.xmj.config;

import cn.xmj.id.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdWorkerConfig {

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(21, 10, 0);
    }
    
}
