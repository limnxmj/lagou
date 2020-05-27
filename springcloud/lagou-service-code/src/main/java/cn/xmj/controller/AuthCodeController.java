/**
 * FileName: AuthCodeController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 9:28
 * Description:
 */
package cn.xmj.controller;

import cn.xmj.feignclient.EmailFeignClient;
import cn.xmj.service.AuthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/code")
public class AuthCodeController {

    @Autowired
    private AuthCodeService authCodeService;

    @Autowired
    private EmailFeignClient emailFeignClient;

    /**
     * ⽣成验证码并发送到对应邮箱，成功true，失败false
     */
    @RequestMapping("/create/{email}")
    public boolean create(@PathVariable String email) {

        //1.生成6位验证码
        int randomCode = randomCode();

        //2.保存到数据库
        authCodeService.saveAuthCode(email, randomCode);

        //3.发送邮件
        return emailFeignClient.email(email, String.valueOf(randomCode));
    }

    private int randomCode() {
        return new Random().nextInt(899999) + 100000;
    }

    /**
     * 校验验证码是否正确，0正确1错误2超时
     */
    @RequestMapping("/validate/{email}/{code}")
    public Integer validate(@PathVariable String email, @PathVariable String code) {
        return authCodeService.validateCode(email, code);
    }
}
