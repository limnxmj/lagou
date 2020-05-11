/**
 * FileName: MyWebAppConfigurer.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/11 11:51
 * Description:
 */
package cn.xmj.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                new IpInterceptor())
                .addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
