import cn.xmj.ShardingJdbcApplication;
import cn.xmj.entity.COrder;
import cn.xmj.repository.COrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * FileName: Test.java
 * Author:   limn_xmj@163.com
 * Date:     2020/6/28 10:00
 * Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingJdbcApplication.class)
public class TestHaHa {
    @Resource
    private COrderRepository cOrderRepository;

    @Test
    @Repeat(20)
    public void testAdd(){
        Random random = new Random();
        int userId = random.nextInt(100);
        int companyId = random.nextInt(3);
        COrder cOrder = new COrder();
        cOrder.setDel(false);
        cOrder.setUserId(userId);
        cOrder.setCompanyId(companyId);
        cOrder.setPublishUserId(1000);
        cOrder.setPositionId(1001);
        cOrder.setResumeType(0);
        cOrder.setStatus("AUTO_FILTER");
        cOrder.setCreateTime(new Date());
        cOrder.setUpdateTime(new Date());
        cOrderRepository.save(cOrder);
    }

    @Test
    public void testFind(){
        List<COrder> cOrders = cOrderRepository.findAll();
//        cOrders.forEach(cOrder->{
//            System.out.println(cOrder);
//        });

        cOrders = cOrderRepository.findAll();
        cOrders = cOrderRepository.findAll();
        cOrders = cOrderRepository.findAll();
//        cOrders.forEach(cOrder->{
//            System.out.println(cOrder);
//        });
    }
}
