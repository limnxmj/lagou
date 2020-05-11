/**
 * FileName: IpController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/11 9:20
 * Description:
 */
package cn.xmj.controller;

import cn.xmj.service.IpServiceB;
import cn.xmj.service.IpServiceC;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ip")
public class IpController {

    @Reference
    private IpServiceB ipServiceB;

    @Reference
    private IpServiceC ipServiceC;


    @RequestMapping("/printIp")
    @ResponseBody
    public String printIp(HttpServletRequest request) {
        String ipB = ipServiceB.printIp();
        String ipC = ipServiceC.printIp();
        return ipB + "\n" + ipC;
    }
}
