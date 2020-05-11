/**
 * FileName: MonitorServiceImpl.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/11 14:12
 * Description:
 */
package cn.xmj.service.impl;

import cn.xmj.service.MonitorService;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

@Service
public class MonitorServiceImpl implements MonitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorService.class);

    private void sleep(String methodName) {
        int i = new Random().nextInt(101);
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        LOGGER.info("MonitorService {} sleep {} ms", methodName, i);
    }

    @Override
    public void methodA() {
        sleep("methodA");
    }

    @Override
    public void methodB() {
        sleep("methodB");
    }

    @Override
    public void methodC() {
        sleep("methodC");
    }
}
