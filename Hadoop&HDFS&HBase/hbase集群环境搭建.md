**一、环境**

xmjmaster: 192.168.238.160

xmjslave1: 192.168.238.161

xmjslave2: 192.168.238.162

**二、zookeeper集群安装部署**

**1.下载zookeeper**

~~~
https://zookeeper.apache.org/releases.html 
zookeeper-3.4.14.tar.gz
~~~

**2.上传到linux并解压缩**

~~~
tar -zxvf zookeeper-3.4.14.tar.gz -C /usr/local/
~~~

**3.进入zookeeper目录，创建data和logs目录**

~~~
cd /usr/local/zookeeper-3.4.14
mkdir data
cd data
mkdir logs
~~~

**4.修改配置文件zoo.cfg**

~~~
cd /usr/local/zookeeper-3.4.14/conf
mv zoo_sample.cfg zoo.cfg

vim zoo.cfg

dataDir=/usr/local/zookeeper-3.4.14/data
dataLogDir=/usr/local/zookeeper-3.4.14/data/logs
~~~

**5.集群配置：myid**

~~~
cd /usr/local/zookeeper-3.4.14/data
touch myid
vim myid ##记录每个服务器的id,内容分别为1、2、3
1
~~~

**6.集群配置：修改配置文件zoo.cfg**

~~~
cd /usr/local/zookeeper-3.4.14/conf
vim zoo.cfg
server.1=192.168.238.160:2881:3881
server.2=192.168.238.161:2881:3881
server.3=192.168.238.162:2881:3881
~~~

**7.分发zookeeper目录到其他节点(注意修改对应的myid)**

~~~
cd /usr/local/
scp -r zookeeper-3.4.14 192.168.238.161:/usr/local/
scp -r zookeeper-3.4.14 192.168.238.162:/usr/local/
~~~

**8.启动**

~~~
cd /usr/local/zookeeper-3.4.14/bin
./zkServer.sh start
~~~

**三、hbase集群安装部署**

**1.下载hbase**

~~~
http://archive.apache.org/dist/ 
hbase-1.3.1-bin.tar.gz
~~~

**2.上传到linux并解压缩**

~~~
tar -zxvf hbase-1.3.1-bin.tar.gz -C /usr/local/
~~~

**3.修改配置文件**

**1) 把hadoop中的配置core-site.xml 、hdfs-site.xml拷贝到hbase安装目录下的conf文件夹中**

~~~
cd /usr/local/hadoop-2.7.2/etc/hadoop/
cp core-site.xml hdfs-site.xml /usr/local/hbase-1.3.1/conf/
~~~

**2)修改 hbase-env.sh**

~~~
cd /usr/local/hbase-1.3.1/conf/
vim hbase-env.sh

#添加java环境变量 
export JAVA_HOME=/usr/java/jdk1.8.0_251-amd64 
#指定使用外部的zk集群 
export HBASE_MANAGES_ZK=FALSE
~~~

**3)修改 hbase-site.xml**

~~~
<!-- 指定hbase在HDFS上存储的路径 --> 
<property>
    <name>hbase.rootdir</name> 
    <value>hdfs://xmjmaster:9000/hbase</value> 
</property> 
<!-- 指定hbase是分布式的 --> 
<property>
    <name>hbase.cluster.distributed</name> 
    <value>true</value> 
</property> 
<!-- 指定zk的地址，多个用“,”分割 --> 
<property>
    <name>hbase.zookeeper.quorum</name> 
    <value>xmjmaster:2181,xmjslave1:2181,xmjslave2:2181</value> 
</property>
~~~

**4)修改regionservers**

~~~
vim regionservers

xmjmaster
xmjslave1
xmjslave2
~~~

**5)hbase的conf目录下创建文件backup-masters**

~~~
vim backup-masters
xmjslave1
~~~

**4.配置hbase环境变量**

~~~
vim /etc/profile

export HBASE_HOME=/usr/local/hbase-1.3.1
export PATH=$PATH:$HBASE_HOME/bin

source /etc/profile
~~~

**5. 分发hbase目录和环境变量到其他节点**

~~~
cd /usr/local/
scp -r hbase-1.3.1 192.168.238.161:/usr/local/
scp -r hbase-1.3.1 192.168.238.162:/usr/local/
~~~

**四、hbase集群的启动和停止**

~~~
##前提条件：先启动hadoop和zk集群
cd /usr/local/hbase-1.3.1/bin/
./start-hbase.sh
~~~

~~~
访问hbase集群的web管理界面：HMaster的主机名:16010
http://xmjmaster:16010
~~~

![image-20200722104512347](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200722104512347.png)



