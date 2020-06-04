import com.lagou.edu.EmailApplication8082;
import com.lagou.edu.service.EmailService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = {EmailApplication8082.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class EmailTest {

    @Reference
    EmailService emailService;


    @Test
    public void testSendEmail() {
      emailService.sendSimpleMail("limn_xmj@163.com","AuthCode","8888888");
    }
}
