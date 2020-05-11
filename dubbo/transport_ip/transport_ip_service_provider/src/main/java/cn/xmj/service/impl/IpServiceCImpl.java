/**
 * FileName: IpServiceBImpl.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/11 10:51
 * Description:
 */
package cn.xmj.service.impl;

import cn.xmj.service.IpServiceC;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class IpServiceCImpl implements IpServiceC {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpServiceC.class);
    private static final String REQUEST_IP = "requestIp";

    @Override
    public String printIp() {
        String requestIp =  RpcContext.getContext().getAttachment(REQUEST_IP);
        LOGGER.info("IpServiceCImpl requestIp:{}", requestIp);
        return "serviceC requestIp:" + requestIp;
    }
}