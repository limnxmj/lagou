/**
 * FileName: ZkClientUtil.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/29 22:20
 * Description:
 */
package com.lagou.zookeeper;

import com.lagou.dao.IpDao;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

public class ZkClientUtil {

    private final String ZK_SERVER = "127.0.0.1:2181";

    private final String ZK_BASE = "/rpcServer";

    private final ZkClient zkClient;

    public ZkClientUtil() {
        this.zkClient = new ZkClient(ZK_SERVER, 300000, 60000);
    }

    public String getZkBase() {
        return ZK_BASE;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void registeEphemeralServer(String host, int port) {
        boolean exists = this.zkClient.exists(ZK_BASE);
        if (!exists) {
            this.zkClient.createPersistent(ZK_BASE);
        }
        this.zkClient.createEphemeral(ZK_BASE + "/" + host + ":" + port, new IpDao(host, port));
    }

    public List<IpDao> getServerList() {
        List<IpDao> result = new ArrayList<>();
        try {
            List<String> children = this.zkClient.getChildren(ZK_BASE);
            if (children != null && !children.isEmpty()) {
                for (String child : children) {
                    result.add(new IpDao(child.split(":")[0], Integer.parseInt(child.split(":")[1])));
                }
            }
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public String getServer() {

        String server = "";
        List<String> children = this.zkClient.getChildren(ZK_BASE);
        if (children != null && !children.isEmpty()) {
            IpDao resultDao = null;
            for (String child : children) {
                Stat stat = new Stat();
                String path = ZK_BASE + "/" + child;
                IpDao ipDao = this.zkClient.readData(path, stat);

                //时间清零
                if (ipDao.getTime() != 0 && System.currentTimeMillis() - ipDao.getLastTime() > 5000) {
                    ipDao.setTime(0);
                    this.zkClient.writeData(path, ipDao);
                }
                if (resultDao == null || ipDao.getTime() == 0 || (ipDao.getTime() != 0 && ipDao.getTime() < resultDao.getTime())) {
                    resultDao = ipDao;
                }
            }
            return resultDao.getIp() + ":" + resultDao.getPort();
        }
        return server;
    }


    public void writeData(String path, long time, long lastTime) {
        try {
            path = ZK_BASE + "/" + path;
            IpDao ipDao = this.zkClient.readData(path);
            ipDao.setTime(time);
            ipDao.setLastTime(lastTime);
            System.out.println(ipDao);
            this.zkClient.writeData(path, ipDao);
        } catch (Exception e) {
        }
    }
}
