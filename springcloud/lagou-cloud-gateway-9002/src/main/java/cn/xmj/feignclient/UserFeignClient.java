/**
 * FileName: UserFeignClient.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 17:41
 * Description:
 */
package cn.xmj.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "lagou-service-user")
public interface UserFeignClient {

    @RequestMapping(value = "/user/info/{token}", method = RequestMethod.GET)
    public String info(@PathVariable(value = "token") String token);
}
