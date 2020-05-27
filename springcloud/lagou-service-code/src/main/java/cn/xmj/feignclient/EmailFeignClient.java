/**
 * FileName: EmailFeignClient.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/27 11:38
 * Description:
 */
package cn.xmj.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "lagou-service-email")
public interface EmailFeignClient {

    @RequestMapping(value = "/email/{email}/{code}", method = RequestMethod.GET)
    public boolean email(@PathVariable(value = "email") String email, @PathVariable(value = "code") String code);

}
