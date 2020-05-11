/**
 * FileName: DubboConsumerMain.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/11 14:41
 * Description:
 */
package cn.xmj;

import cn.xmj.bean.MonitorComponent;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DubboConsumerMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        MonitorComponent monitor = context.getBean(MonitorComponent.class);

        ExecutorService executorService = Executors.newFixedThreadPool(16);
        while (true) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    monitor.methodA();
                    monitor.methodB();
                    monitor.methodC();
                }
            });
        }
    }

    @Configuration
    @PropertySource("classpath:/dubbo-consumer.properties")
    @ComponentScan("cn.xmj.bean")
    @EnableDubbo
    static class ConsumerConfiguration {

    }
}
