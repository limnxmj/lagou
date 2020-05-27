/**
 * FileName: UserController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 8:58
 * Description:
 */
package cn.xmj.controller;

import cn.xmj.pojo.Token;
import cn.xmj.service.TokenService;
import cn.xmj.util.CookiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TokenService tokenService;

    /**
     * 注册接⼝，true成功，false失败
     */
    @RequestMapping(value = "/register/{email}/{password}/{code}")
    public boolean register(@PathVariable String email, @PathVariable String password, @PathVariable String code,
                            HttpServletResponse response) {

        if (tokenService.getTokenByEmail(email) != null) {
            return false;
        }

        String token = UUID.randomUUID().toString();
        tokenService.saveToken(email, token);

        CookiesUtil.setCookie(response, "token", token);
        CookiesUtil.setCookie(response, "email", email);
        return true;
    }

    /**
     * 是否已注册，根据邮箱判断,true代表已经注册过，false代表尚未注册
     */
    @RequestMapping("/isRegistered/{email}")
    public boolean isRegistered(@PathVariable String email) {
        return null != tokenService.getTokenByEmail(email);
    }

    /**
     * 登录接⼝，验证⽤户名密码合法性，根据⽤户名和密码⽣成token，token存⼊数据库，并写⼊cookie中，登录成功返回邮箱地址，重定向到欢迎⻚
     */
    @RequestMapping(value = "/login/{email}/{password}")
    public String login(@PathVariable String email, @PathVariable String password,
                        HttpServletResponse response) {
        Token tokenByEmail = tokenService.getTokenByEmail(email);
        //无用户表，判断token为空为未注册，返回空
        if (tokenByEmail == null) {
            return "";
        }

        CookiesUtil.setCookie(response, "token", tokenByEmail.getToken());
        CookiesUtil.setCookie(response, "email", tokenByEmail.getEmail());
        return email;
    }

    /**
     * 根据token查询⽤户登录邮箱接⼝
     */
    @RequestMapping("/info/{token}")
    public String info(@PathVariable String token) {
        return tokenService.getEmailByToken(token);
    }
}
