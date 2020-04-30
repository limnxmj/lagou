/**
 * FileName: ZkClientUtil.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/29 22:20
 * Description:
 */
package cn.xmj.zookeeper;

import cn.xmj.dto.ZkDatasourceData;
import cn.xmj.serializer.CustomSerializer;
import com.alibaba.druid.util.StringUtils;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkClientUtil.class);

    private final String ZK_SERVER = "192.168.238.156:2181";

    private final String ZK_BASE = "/datasource";

    private final ZkClient zkClient;

    public ZkClientUtil() {
        this.zkClient = new ZkClient(ZK_SERVER, 300000, 60000, new CustomSerializer());
        init();
    }

    public String getZkBase() {
        return ZK_BASE;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    private void init() {

        if (!this.zkClient.exists(ZK_BASE)) {
            this.zkClient.createPersistent(ZK_BASE);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("driverClassName=com.mysql.jdbc.Driver").append(";");
        sb.append("url=jdbc:mysql://localhost:3306/test?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true").append(";");
        sb.append("username=root").append(";");
        sb.append("password=root").append(";");

        LOGGER.info("sb={}", sb.toString());

        this.zkClient.writeData(ZK_BASE, sb.toString());
    }

    public ZkDatasourceData getDataSourceData() {
        String data = zkClient.readData(ZK_BASE);
        return parseData(data);
    }

    public ZkDatasourceData parseData(String data) {
        if (StringUtils.isEmpty(data)) {
            throw new RuntimeException("zookeeper数据源配置错误");
        }
        String[] datas = data.split(";");
        if (datas.length < 4) {
            throw new RuntimeException("zookeeper数据源配置错误");
        }
        try {
            ZkDatasourceData zkDatasourceData = new ZkDatasourceData();
            zkDatasourceData.setDriverClassName(datas[0].substring(datas[0].indexOf("=") + 1));
            zkDatasourceData.setUrl(datas[1].substring(datas[1].indexOf("=") + 1));
            zkDatasourceData.setUsername(datas[2].substring(datas[2].indexOf("=") + 1));
            zkDatasourceData.setPassword(datas[3].substring(datas[3].indexOf("=") + 1));
            return zkDatasourceData;
        } catch (Exception e) {
            throw new RuntimeException("zookeeper数据源配置错误", e);
        }
    }

}
