**一、集群环境**

redis: redis-5.0.5

centos ip: 192.168.238.160  

三主三从：7001~7006

扩容一主一从：7007~7008

**二、集群环境搭建**

**1.下载并安装**

~~~
cd /usr/local/
mkdir -p redis/redis-cluster/7001
cd redis/redis-cluster/
wget http://download.redis.io/releases/redis-5.0.5.tar.gz
tar -zxvf redis-5.0.5.tar.gz
cd redis-5.0.5/src/
make install PREFIX=/usr/local/redis/redis-cluster/7001
~~~

**2.修改7001实例配置文件redis.conf**

~~~
cp /usr/local/redis/redis-cluster/redis-5.0.5/redis.conf /usr/local/redis/redis-cluster/7001/bin/
~~~

~~~
cd /usr/local/redis/redis-cluster/7001/bin/
vim redis.conf

#bind 127.0.0.1
protected-mode no
port 7001
daemonize yes
cluster-enabled yes
~~~

**3.复制7001实例至7002~7006实例，并修改对应redis.conf端口号**

~~~
cp -r 7001 7002
cp -r 7001 7003
cp -r 7001 7004
cp -r 7001 7005
cp -r 7001 7006
~~~

**4.创建start.sh, 启动所有实例**

~~~
vim start.sh

cd 7001/bin
./redis-server redis.conf
cd ../../7002/bin
./redis-server redis.conf
cd ../../7003/bin
./redis-server redis.conf
cd ../../7004/bin
./redis-server redis.conf
cd ../../7005/bin
./redis-server redis.conf
cd ../../7006/bin
./redis-server redis.conf
~~~

~~~
#赋写和执行的权限
chmod u+x start.sh
#启动
./start.sh
~~~

![image-20200802181945640](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802181945640.png)

**5.创建redis集群**

~~~
cd 7001/bin

./redis-cli --cluster create 192.168.238.160:7001 192.168.238.160:7002 192.168.238.160:7003 192.168.238.160:7004 192.168.238.160:7005 192.168.238.160:7006 --cluster-replicas 1
~~~

![image-20200802182440510](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802182440510.png)

![image-20200802182758518](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802182758518.png)

**6.客户端连接集群**

~~~
./redis-cli -h 127.0.0.1 -p 7001 -c
~~~

![image-20200802183148144](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802183148144.png)

~~~
#查看集群状态
192.168.238.160:7002> cluster info
~~~

![image-20200802183243638](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802183243638.png)

**三、扩容**

**1.创建7007节点，并修改配置redis.conf**

~~~
cd /usr/local/redis/redis-cluster/
mkdir 7007
cd redis-5.0.5/src/
make install PREFIX=/usr/local/redis/redis-cluster/7007
~~~

~~~
cp /usr/local/redis/redis-cluster/redis-5.0.5/redis.conf /usr/local/redis/redis-cluster/7007/bin/
cd ../../7007/bin/
~~~

~~~
vim redis.conf

#bind 127.0.0.1
protected-mode no
port 7007
daemonize yes
cluster-enabled yes
~~~

**2.复制7007节点至7008节点并修改redis.conf端口号**

~~~
cp -r 7007 7008
~~~

**3.添加7007节点作为新节点,并启动**

~~~
cd ../../7007/bin/
./redis-server redis.conf
~~~

~~~
cd ../../7001/bin/
./redis-cli --cluster add-node 192.168.238.160:7007 192.168.238.160:7001
~~~

![image-20200802185027719](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802185027719.png)

![image-20200802185120167](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802185120167.png)

**4.查看集群节点**

~~~
./redis-cli -p 7001 -c
cluster nodes
~~~

![image-20200802185315928](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802185315928.png)

**5.给7007结点分配槽**

~~~
./redis-cli --cluster reshard 192.168.238.160:7007
~~~

![image-20200802185829669](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802185829669.png)

**6.添加7008从节点，将7008作为7007的从节点**

~~~
cd ../../7008/bin
./redis-server redis.conf
~~~

~~~
cd ../../7001/bin
./redis-cli --cluster add-node 192.168.238.160:7008 192.168.238.160:7007 --cluster-slave --cluster-master-id a1e88a5c1193fb690e874d8b2b808035761f45dd
~~~

![image-20200802190217681](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802190217681.png)

![image-20200802190401353](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802190401353.png)

~~~
./redis-cli -p 7001 -c
cluster nodes
~~~

![image-20200802190450037](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200802190450037.png)