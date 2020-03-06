/**
 * FileName: Transactional.java
 * Author:   limn_xmj@163.com
 * Date:     2020/3/5 21:44
 * Description:
 */
package com.lagou.edu.dao.annos;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transactional {

    String value() default "";

}
