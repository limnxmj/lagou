**注：jdk1.8已安装配置**

**一、elasticsearch安装配置**

**1.下载elasticsearch**

~~~
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.3.0-linux-x86_64.tar.gz
~~~

**2.解压缩**

~~~
tar -zxvf elasticsearch-7.3.0-linux-x86_64.tar.gz -C /usr/local
cd /usr/local
mv elasticsearch-7.3.0/ elasticsearch
~~~

**3.配置elasticsearch**

~~~yml
1)编辑elasticsearch.yml
vim /usr/local/elasticsearch/config/elasticsearch.yml
 
node.name: node-1
network.host: 0.0.0.0
http.port: 9200
cluster.initial_master_nodes: ["node-1"]
~~~

~~~
2)按需修改jvm.options内存设置
vim /usr/local/elasticsearch/config/jvm.options
~~~

~~~
3)添加es用户，es默认root用户无法启动，需要改为其他用户
useradd estest 
#修改密码 
passwd estest

#改变es目录拥有者账号
chown -R estest /usr/local/elasticsearch/
~~~

~~~
4)修改sysctl.conf
vim /etc/sysctl.conf

#末尾添加(执行sysctl -p 让其生效)：
vm.max_map_count=655360
~~~

~~~
5)修改limits.conf
vim /etc/security/limits.conf

#末尾添加:
* soft nofile 65536 
* hard nofile 65536 
* soft nproc 4096 
* hard nproc 4096
~~~

**4.启动es**

~~~
su estest
/usr/local/elasticsearch/bin/elasticsearch
~~~

**5.测试**

~~~
http://xmj:9200
~~~

![image-20200921153124366](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200921153124366.png)

**二、kibana安装配置**

**1.下载kibana**

~~~
wget https://artifacts.elastic.co/downloads/kibana/kibana-7.3.0-linux-x86_64.tar.gz
~~~

**2.解压缩**

~~~
tar -zxvf kibana-7.3.0-linux-x86_64.tar.gz -C /usr/local
cd /usr/local
mv kibana-7.3.0-linux-x86_64/ kibana
~~~

**3.配置kibana**

~~~
1)改变es目录拥有者账号
chown -R estest /usr/local/kibana/
~~~

~~~
2)设置访问权限
chmod -R 777 /usr/local/kibana/
~~~

~~~yml
3)修改配置文件kibana.yml
vim /usr/local/kibana/config/kibana.yml

server.port: 5601
server.host: "0.0.0.0"
elasticsearch.hosts: ["http://192.168.238.170:9200"]
~~~

**4.启动**

~~~
1)方式一：
su estest
/usr/local/kibana/bin/kibana
~~~

~~~
2)方式二：
/usr/local/kibana/bin/kibana --allow-root
~~~

**5.访问**

~~~
http://xmj:5601
~~~

![image-20200921154742455](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200921154742455.png)

