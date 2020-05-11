/**
 * FileName: IpInterceptor.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/11 11:49
 * Description:
 */
package cn.xmj.interceptor;

import cn.xmj.utils.IpUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IpInterceptor implements HandlerInterceptor {

    private static final String REQUEST_IP = "requestIp";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestIp = IpUtils.getIp(request);
        MDC.put(REQUEST_IP, requestIp);
        return true;
    }
}
