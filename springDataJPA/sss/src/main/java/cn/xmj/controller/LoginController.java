/**
 * FileName: LoginController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/16 10:43
 * Description:
 */
package cn.xmj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class LoginController {

    private static final Map<String, String> userMap = new HashMap<>();
    static{
        userMap.put("admin", "admin");
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/loginCheck")
    @ResponseBody
    public Map<String, Object> loginCheck(String username, String password) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", false);
        if (username == null || "".equals(username)) {
            resultMap.put("message", "用户名为空");
            return resultMap;
        }
        if (password == null || "".equals(password)) {
            resultMap.put("message", "密码为空");
            return resultMap;
        }
        if (userMap.get(username) == null) {
            resultMap.put("message", "用户不存在");
            return resultMap;
        }
        if (!userMap.get(username).equals(password)) {
            resultMap.put("message", "密码不正确");
            return resultMap;
        }
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest request, String username, String password) {
        request.getSession().setAttribute("username", username);
        request.getSession().setAttribute("password", password);
        return "redirect:/resume/findAll";
    }
}
