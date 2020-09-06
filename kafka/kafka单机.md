**一、安装环境**

~~~
1.操作系统：CentOS Linux release 7.5.1804
2.kafka:
3.zookeeper:zookeeper-3.4.14
~~~

**二、安装配置**

**1.zookeeper安装配置**

**2.kafka安装**

~~~
1）上传kafka_2.12-1.0.2.tgz到服务器并解压
tar -zxf kafka_2.12-1.0.2.tgz -C /usr/local
~~~

~~~
2）配置环境变量并生效
vim /etc/profile

export KAFKA_HOME=/usr/local/kafka_2.12-1.0.2
export PATH=$PATH:$KAFKA_HOME/bin

source /etc/profile
~~~

~~~
3）配置/usr/local/kafka_2.12-1.0.2/config中的server.properties文件

zookeeper.connect=localhost:2181/myKafka
log.dirs=/var/xmj/kafka/kafka-logs
~~~

~~~
4）启动kafka
kafka-server-start.sh ../config/server.properties
kafka-server-start.sh -daemon ../config/server.properties

##停止kafka
kafka-server-stop.sh
~~~

![image-20200902151411039](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200902151411039.png)

~~~
5）查看zk节点
~~~

![image-20200902151322101](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200902151322101.png)

