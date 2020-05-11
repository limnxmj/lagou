/**
 * FileName: TransportIPFilter.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/11 9:28
 * Description:
 */
package cn.xmj.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Activate(group = {CommonConstants.CONSUMER})
public class TransportIPFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransportIPFilter.class);
    private static final String REQUEST_IP = "requestIp";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String requestIp = MDC.get(REQUEST_IP);
        if (StringUtils.isBlank(requestIp)) {
            requestIp = StringUtils.EMPTY_STRING;
        }
        LOGGER.info("TransportIPFilter requestIp={}", requestIp);
        RpcContext.getContext().setAttachment(REQUEST_IP, requestIp);
        return invoker.invoke(invocation);
    }
}
