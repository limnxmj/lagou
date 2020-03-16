/**
 * FileName: LoginIntercepter.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/16 10:17
 * Description:
 */
package cn.xmj.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class LoginInterceptor implements HandlerInterceptor {

    //不拦截的url
    private List<String> excludeUrls;

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (excludeUrls != null && !excludeUrls.isEmpty()) {
            for (String excludeUrl : excludeUrls) {
                if (request.getRequestURI().indexOf(excludeUrl) >= 0) {
                    return true;
                }
            }
        }

        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    private boolean isAjax(HttpServletRequest req) {
        String requestType = req.getHeader("X-Requested-With");
        if (requestType == null || !"XMLHttpRequest".equals(requestType)) {
            return false;
        }
        return true;
    }

}
