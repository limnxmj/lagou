/**
 * FileName: TokenFilter.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 16:06
 * Description:
 */
package cn.xmj.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenFilter implements GlobalFilter, Ordered {

    private static Set<String> execuleUrls = new HashSet<>();

    static {
        execuleUrls.add("/user");
        execuleUrls.add("/code");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String clientIp = request.getRemoteAddress().getHostString();
        String uri = request.getURI().toString();
        if (needFilter(uri)) {
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            HttpCookie token = (HttpCookie) cookies.get("token");
            if (token == null || StringUtils.isEmpty(token.getValue())) {
                response.setStatusCode(HttpStatus.FORBIDDEN); // 状态码
                String data = "Request be denied!";
                DataBuffer wrap = response.bufferFactory().wrap(data.getBytes());
                return response.writeWith(Mono.just(wrap));
            }
        }
        return chain.filter(exchange);
    }

    private boolean needFilter(String uri) {
        for (String execuleUrl : execuleUrls) {
            if (uri.indexOf(execuleUrl) > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
