/**
 * FileName: IpFilter.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 15:34
 * Description:
 */
package cn.xmj.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Component  // 让容器扫描到，等同于注册了
@RefreshScope
public class IpFilter implements GlobalFilter, Ordered {

    //1000次
    @Value("${limitCount}")
    private int limitCount;
    //1分钟
    @Value("${limitTime}")
    private int limitTime;

    private Map<String, Integer> countMap = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String clientIp = request.getRemoteAddress().getHostString();
        String uri = request.getURI().toString();
        String key = clientIp;
        if (uri.indexOf("/user/register/") > 0) {
            if (countMap.get(key) == null || countMap.get(key) == 0) {
                countMap.put(key, 1);
            } else {
                countMap.put(key, countMap.get(key) + 1);
            }
            int count = countMap.get(key);
            if (count > 0) {
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        countMap.remove(key);
                    }
                };
                timer.schedule(timerTask, limitTime);
            }
            if (count > limitCount) {
                response.setStatusCode(HttpStatus.FORBIDDEN); // 状态码
                String data = "您频繁进⾏注册，请求已被拒绝!";
                DataBuffer wrap = response.bufferFactory().wrap(data.getBytes());
                return response.writeWith(Mono.just(wrap));
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}

