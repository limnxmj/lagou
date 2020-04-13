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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        System.out.println("===============>>>>>>当前uri：" + request.getRequestURI());
        Object username = session.getAttribute("username");
        System.out.println("=================>>>>>username:" + username);
        if(username == null) {
            // 没有登录,重定向到登录页
            System.out.println("未登录，请登录");
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }else{
            System.out.println("已登录，放行请求");
            // 已登录，放行
            return true;
        }
    }
}
