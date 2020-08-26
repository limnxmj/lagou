**一、安装环境**

~~~
1.操作系统：CentOS Linux release 7.5.1804
2.Erlang：erlang-23.0.2-1.el7.x86_64
3.RabbitMQ：rabbitmq-server-3.8.4-1.el7.noarch
~~~

**二、安装配置**

**1.安装依赖**

~~~
yum install socat -y
~~~

**2.安装Erlang**

~~~
wget https://github.com/rabbitmq/erlang-rpm/releases/download/v23.0.2/erlang-23.0.2-1.el7.x86_64.rpm
rpm -ivh erlang-23.0.2-1.el7.x86_64.rpm
~~~

**3.安装RabbitMQ**

~~~
wget https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.8.4/rabbitmq-server-3.8.4-1.el7.noarch.rpm
rpm -ivh rabbitmq-server-3.8.4-1.el7.noarch.rpm
~~~

**4.启用RabbitMQ的管理插件**

~~~
cd /usr/lib/rabbitmq/
rabbitmq-plugins enable rabbitmq_management
~~~

**5.开启RabbitMQ**

~~~
#1.
systemctl start rabbitmq-server

#2.
rabbitmq-server

#3.后台启动
rabbitmq-server -detached
~~~

~~~
#访问管理界面
http://xmjmaster:15672/
~~~

![image-20200822161244228](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200822161244228.png)



**6.添加用户**

~~~
rabbitmqctl add_user root 123456
~~~

**7.给用户添加权限**

~~~
rabbitmqctl set_permissions --vhost / root ".*" ".*" ".*"
~~~

**8.给用户设置标签**

~~~
#rabbitmqctl list_users
rabbitmqctl set_user_tags root administrator
~~~

