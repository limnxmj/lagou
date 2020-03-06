/**
 * FileName: Service.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/5 21:43
 * Description:
 */
package com.lagou.edu.dao.annos;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {

    String value() default "";

}
