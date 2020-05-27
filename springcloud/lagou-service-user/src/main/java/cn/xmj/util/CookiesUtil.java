/**
 * FileName: CookiesUtil.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 12:08
 * Description:
 */
package cn.xmj.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookiesUtil {


    public static String getValue(HttpServletRequest request, String cookieName){
        if (StringUtils.isEmpty(cookieName)) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookieName.equalsIgnoreCase(cookies[i].getName())) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expiry);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
