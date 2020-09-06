**一、安装环境**

~~~
1.操作系统：CentOS Linux release 7.5.1804
2.软件版本：
	kafka:kafka_2.12-1.0.2
	zookeeper:zookeeper-3.4.14
	nginx:1.15.6
~~~

| host                       | kafka | zookeeper | nginx |
| -------------------------- | ----- | --------- | ----- |
| xmj(192.168.238.170)       |       |           | √     |
| xmjmaster(192.168.238.160) | √     | √         |       |
| xmjslave1(192.168.238.161) | √     | √         |       |
| xmjslave2(192.168.238.162) | √     | √         |       |

**二、安装配置**

**1.zookeeper安装配置**

~~~
1)解压缩
tar -zxf zookeeper-3.4.14.tar.gz -C /usr/local
~~~

~~~
2）配置文件
cd /usr/local/zookeeper-3.4.14/conf
cp zoo_sample.cfg zoo.cfg 
vim zoo.cfg 

#设置dataDir和dataLogDir
dataDir=/usr/local/zookeeper-3.4.14/data
dataLogDir=/usr/local/zookeeper-3.4.14/data/logs

#添加集群配置
server.1=192.168.238.160:2881:3881
server.2=192.168.238.161:2881:3881
server.3=192.168.238.162:2881:3881
~~~

~~~
3）配置myid
cd /usr/local/zookeeper-3.4.14/data
vim myid 
(xmjmaster:1,xmjslave1:2,xmjslave2:3)
~~~

~~~
4)配置环境变量
vim /etc/profile
export ZOOKEEPER_PREFIX=/usr/local/zookeeper-3.4.14
export PATH=$PATH:$ZOOKEEPER_PREFIX/bin
source /etc/profile
~~~

~~~
5)启动zookeeper集群
zkServer.sh start
##查看集群状态
zkServer.sh status
~~~



![image-20200906085445043](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200906085445043.png)

![image-20200906085525204](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200906085525204.png)

![image-20200906085459885](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200906085459885.png)



**2.kafka安装配置**

~~~
1）上传kafka_2.12-1.0.2.tgz到服务器并解压(xmjmaster)
tar -zxf kafka_2.12-1.0.2.tgz -C /usr/local
~~~

~~~
2)拷贝到xmjslave1和xmjslave2
scp -r /usr/local/kafka_2.12-1.0.2/ xmjslave1:/usr/local
scp -r /usr/local/kafka_2.12-1.0.2/ xmjslave2:/usr/local
~~~

~~~
3）配置环境变量并生效
vim /etc/profile

export KAFKA_HOME=/usr/local/kafka_2.12-1.0.2
export PATH=$PATH:$KAFKA_HOME/bin

source /etc/profile
~~~

~~~
4）配置/usr/local/kafka_2.12-1.0.2/config中的server.properties文件
vim /usr/local/kafka_2.12-1.0.2/config/server.properties

##broker.id (xmjmaster:0 xmjslave1:1 xmjslave2:2)
broker.id=0 
listeners=PLAINTEXT://:9092 
##填写对应host
advertised.listeners=PLAINTEXT://xmjmaster:9092 
log.dirs=/var/xmj/kafka/kafka-logs
zookeeper.connect=xmjmaster:2181,xmjslave1:2181,xmjslave2:2181/myKafka

~~~

~~~
5）启动kafka
kafka-server-start.sh /usr/local/kafka_2.12-1.0.2/config/server.properties
kafka-server-start.sh -daemon /usr/local/kafka_2.12-1.0.2/config/server.properties

##停止kafka
kafka-server-stop.sh
~~~

~~~
5）查看zk节点
ls /myKafka/brokers/ids
get /myKafka/brokers/ids/0
get /myKafka/brokers/ids/1
get /myKafka/brokers/ids/2
~~~

![image-20200906091730146](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200906091730146.png)

![image-20200906091823693](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200906091823693.png)

![image-20200906091842461](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200906091842461.png)

![image-20200906091856273](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200906091856273.png)

**3.nginx ngx_kafka_module安装配置**

~~~
1)安装librdkafka

git clone https://github.com/edenhill/librdkafka
cd librdkafka
./configure
make
make install
~~~

~~~
2）配置nginx

git clone https://github.com/brg-liuwei/ngx_kafka_module
wget http://nginx.org/download/nginx-1.15.6.tar.gz
cd nginx-1.15.6/
./configure --prefix=/usr/local/nginx --add-module=/usr/local/ngx_kafka_module
make
make install

#错误信息
./nginx: error while loading shared libraries: librdkafka.so.1: cannot open shared object file: No such file or directory

echo "/usr/local/lib" >> /etc/ld.so.conf
ldconfig
~~~

![image-20200906155715796](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200906155715796.png)

**三、测试**

~~~
zkServer.sh start
zkServer.sh status

kafka-server-start.sh -daemon /usr/local/kafka_2.12-1.0.2/config/server.properties

kafka-topics.sh --zookeeper localhost/myKafka --list

kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic tp_individual

curl http://localhost/kafka/tp_individual -d "message send to kafka topic"

~~~

![image-20200906193133296](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200906193133296.png)





**备注**

正常情况kafka收集日志感觉应该是需要做类似埋点的东西，点击、收藏等操作有自己在后台访问controller做些处理，演示中的就是直接调用的nginx配置的kafka的主题，没有业务处理的部分