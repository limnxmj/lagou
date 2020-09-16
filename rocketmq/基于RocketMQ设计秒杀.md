**一、环境**

~~~
centos 7
jdk 1.8
rocketmq 4.5.1
redis 5.0.5
~~~

**二、rocket环境安装测试**

**1.下载rocketmq**

~~~
wget https://archive.apache.org/dist/rocketmq/4.5.1/rocketmq-all-4.5.1-bin-release.zip
~~~

**2.解压缩**

~~~
unzip rocketmq-all-4.5.1-bin-release.zip -d /usr/local
cd /usr/local
mv rocketmq-all-4.5.1-bin-release/ rocketmq
~~~

**3.配置环境变量**

~~~
vim /etc/profile

export ROCKET_HOME=/usr/local/rocketmq
export PATH=$PATH:$ROCKET_HOME/bin

source /etc/profile
~~~

![image-20200907193742177](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200907193742177.png)

**4.启动**

~~~
# 1.启动NameServer 
mqnamesrv 

# 2.查看启动日志 
tail -f ~/logs/rocketmqlogs/namesrv.log
~~~

~~~
# 1.启动Broker 
mqbroker -n localhost:9876 

# 2.查看启动日志 
tail -f ~/logs/rocketmqlogs/broker.log
~~~

~~~
# 1.关闭NameServer 
mqshutdown namesrv 

# 2.关闭Broker 
mqshutdown broker
~~~

**5.环境测试**

~~~
# 发送消息
# 1.设置环境变量 
export NAMESRV_ADDR=localhost:9876 
# 2.使用安装包的Demo发送消息 
tools.sh org.apache.rocketmq.example.quickstart.Producer

export NAMESRV_ADDR=localhost:9876 && tools.sh org.apache.rocketmq.example.quickstart.Producer
~~~

![image-20200907200828005](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200907200828005.png)

~~~
# 接收消息
# 1.设置环境变量 
export NAMESRV_ADDR=localhost:9876 
# 2.接收消息 
tools.sh org.apache.rocketmq.example.quickstart.Consumer

export NAMESRV_ADDR=localhost:9876 && tools.sh org.apache.rocketmq.example.quickstart.Consumer
~~~

![image-20200907201018807](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200907201018807.png)

**三、redis安装配置**

~~~
#下载解压缩安装redis
wget http://download.redis.io/releases/redis-5.0.5.tar.gz
tar -zxvf redis-5.0.5.tar.gz
cd redis-5.0.5/src/
make install PREFIX=/usr/local/redis-5.0.5
~~~

~~~
#修改配置文件
cp redis.conf /usr/local/redis-5.0.5/bin/
cd /usr/local/redis-5.0.5/bin/
vim redis.conf
#bind 127.0.0.1
protected-mode no
daemonize yes
~~~

~~~
#redis启动
./redis-server redis.conf
~~~

