/**
 * FileName: RedisClientTemplateTest.java
 * Author:   limn_xmj@163.com
 * Date:     2020/8/3 7:31
 * Description:
 */
package cn.xmj;

import cn.xmj.service.RedisClientTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisClientTemplateTest {

    @Autowired
    private RedisClientTemplate redisClientTemplate;

    @Test
    public void testAdd() {
        redisClientTemplate.set("java:name:001", "xmj");
        redisClientTemplate.set("java:name:002", "xmj2");
    }

    @Test
    public void getTest() {
        System.out.println(redisClientTemplate.get("java:name:001"));
        System.out.println(redisClientTemplate.get("java:name:002"));

    }
}
