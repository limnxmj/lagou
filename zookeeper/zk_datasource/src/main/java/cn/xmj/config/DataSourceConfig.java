/**
 * FileName: DataSourceConfig.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/30 14:11
 * Description:
 */
package cn.xmj.config;

import cn.xmj.dto.ZkDatasourceData;
import cn.xmj.zookeeper.ZkClientUtil;
import com.alibaba.druid.pool.DruidDataSource;
import org.I0Itec.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DataSourceConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private ZkClientUtil zkClientUtil;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public DataSource dataSource() {
        zkClientUtil.getZkClient().subscribeDataChanges(zkClientUtil.getZkBase(), new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                LOGGER.info("dataPath={} handleDataChange, data={}", dataPath, data);
                registeBean((String) data);
            }
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                LOGGER.info("dataPath={} handleDataDeleted", dataPath);
            }
        });
        return newDruidDataSource(zkClientUtil.getDataSourceData());
    }

    private DruidDataSource newDruidDataSource(ZkDatasourceData dataSourceData) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dataSourceData.getDriverClassName());
        druidDataSource.setUrl(dataSourceData.getUrl());
        druidDataSource.setUsername(dataSourceData.getUsername());
        druidDataSource.setPassword(dataSourceData.getPassword());
        return druidDataSource;
    }

    private void registeBean(String data) {
        try {
            ZkDatasourceData dataSourceData = zkClientUtil.parseData(data);
            DruidDataSource druidDataSource = applicationContext.getBean(DruidDataSource.class);
            if (druidDataSource.isInited()) {
                druidDataSource.restart();
            }
            druidDataSource.restart();
            druidDataSource.setDriverClassName(dataSourceData.getDriverClassName());
            druidDataSource.setUrl(dataSourceData.getUrl());
            druidDataSource.setUsername(dataSourceData.getUsername());
            druidDataSource.setPassword(dataSourceData.getPassword());
            druidDataSource.init();
            LOGGER.info("registeBean druidDataSource success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
