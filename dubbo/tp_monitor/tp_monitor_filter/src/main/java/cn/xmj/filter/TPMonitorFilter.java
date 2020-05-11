/**
 * FileName: TPMonitorFilter.java
 * Author:   limn_xmj@163.com
 * Date:     2020/5/11 14:30
 * Description:
 */
package cn.xmj.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.support.RpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Activate(group = {CommonConstants.PROVIDER})
public class TPMonitorFilter implements Filter, Runnable {

    public static final int TOTAL = 100;
    public static final double TP90 = 0.90;
    public static final double TP99 = 0.99;

    private static final Logger LOGGER = LoggerFactory.getLogger(TPMonitorFilter.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final Map<String, Map<Long, Long>> invokesTimeMap = new ConcurrentHashMap<>();

    public TPMonitorFilter() {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(this, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        long beginTime = System.currentTimeMillis();
        Result result = null;
        try {
            result = invoker.invoke(invocation);
        } finally {
            if (!RpcUtils.isAsync(invoker.getUrl(), invocation)) {
                long endTime = System.currentTimeMillis();
                String serviceName = invocation.getAttachment("interface");
                String methodName = invocation.getMethodName();
                long useTime = endTime - beginTime;
                put(serviceName + "_" + methodName, endTime, useTime);
            }
        }
        return result;
    }

    private void put(String key, long dateTime, long useTime) {
        Map<Long, Long> longLongMap = invokesTimeMap.get(key);
        if (longLongMap == null) {
            longLongMap = new HashMap<>();
            invokesTimeMap.put(key, longLongMap);
        }
        longLongMap.put(dateTime, useTime);
    }

    @Override
    public void run() {

        if (invokesTimeMap.isEmpty()) {
            return;
        }
        Map<String, Map<Long, Long>> tempMap = new HashMap<>();
        tempMap.putAll(invokesTimeMap);

        long time = getOneMiniteBeforeDate().getTime();
        for (Map.Entry<String, Map<Long, Long>> entry : tempMap.entrySet()) {
            List<Long> list = new ArrayList<>();

            String key = entry.getKey();
            Map<Long, Long> val = entry.getValue();
            for (Map.Entry<Long, Long> timeEntry : val.entrySet()) {
                long invokeTime = timeEntry.getKey();
                long useTime = timeEntry.getValue();

                //一分钟之前的数据
                if (invokeTime < time) {
                    invokesTimeMap.get(key).remove(invokeTime);
                } else {
                    list.add(useTime);
                }
            }

            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            Collections.sort(list);
            long useTime90 = list.get((int) Math.ceil(list.size() * TP90));
            long useTime99 = list.get((int) Math.ceil(list.size() * TP99));
            LOGGER.info("{} TP90:{}, TP99:{}", key.replace("_", " "), useTime90, useTime99);
        }

    }

    private Date getOneMiniteBeforeDate() {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -1);
        return beforeTime.getTime();
    }

}
