/**
 * FileName: MonitorService.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/11 14:47
 * Description:
 */
package cn.xmj.bean;

import cn.xmj.service.MonitorService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

@Component
public class MonitorComponent {

    @Reference
    private MonitorService monitorService;

    public void methodA(){
        monitorService.methodA();
    }

    public void methodB(){
        monitorService.methodB();
    }

    public void methodC(){
        monitorService.methodC();
    }


}
