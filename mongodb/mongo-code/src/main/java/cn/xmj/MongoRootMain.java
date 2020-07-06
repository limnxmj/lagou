/**
 * FileName: MongoRootMain.java
 * Author:   limn_xmj@163.com
 * Date:     2020/7/6 7:38
 * Description:
 */
package cn.xmj;

import cn.xmj.bean.Resume;
import cn.xmj.dao.ResumeDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class MongoRootMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MongoRootMain.class, args);
        ResumeDao resumeDao = applicationContext.getBean(ResumeDao.class);
        Resume resume = new Resume();

        resume.setName("lisi");
        resume.setCity("北京");
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2000-01-01 12:12:13");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        resume.setBirthday(date);
        resume.setExpectSalary(23000);
        resumeDao.insertResume(resume);
        System.out.println("resume---->" + resume);

        Resume lisi = resumeDao.findByName("lisi");
        System.out.println("lisi:" + lisi);

    }
}
