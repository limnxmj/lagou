**注：jdk1.8已安装配置**

**集群环境**

| 操作系统 | 服务器ip                   | 端口号 | 是否能成为主节点 |
| -------- | -------------------------- | ------ | ---------------- |
| centos7  | 192.168.238.160(xmjmaster) | 9200   | 是               |
| centos7  | 192.168.238.161(xmjslave1) | 9200   | 是               |
| centos7  | 192.168.238.162(xmjslave2) | 9200   | 是               |

**一、elasticsearch安装配置**

**1.下载elasticsearch**

~~~properties
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.3.0-linux-x86_64.tar.gz
~~~

**2.解压缩**

~~~properties
tar -zxvf elasticsearch-7.3.0-linux-x86_64.tar.gz -C /usr/local
cd /usr/local
mv elasticsearch-7.3.0/ elasticsearch
~~~

**3.配置elasticsearch**

~~~properties
1)编辑elasticsearch.yml (xmjmaster)
vim /usr/local/elasticsearch/config/elasticsearch.yml

cluster.name: my-es #集群名称
node.name: node-1 # 节点名称
network.host: 0.0.0.0
http.port: 9200
transport.port: 9300
#初始化一个新的集群时需要此配置来选举master
cluster.initial_master_nodes: ["node-1","node-2","node-3"]
#写入候选主节点的设备地址
discovery.seed_hosts: ["192.168.238.160:9300", "192.168.238.161:9300","192.168.238.160:9300"]
http.cors.enabled: true
http.cors.allow-origin: "*"
~~~

~~~properties
2)按需修改jvm.options内存设置 (xmjmaster)
vim /usr/local/elasticsearch/config/jvm.options
~~~

~~~properties
3)拷贝elasticsearch至 xmjslave1和xmjslave2, 并编辑elasticsearch.yml（参考1）
scp -r /usr/local/elasticsearch/ xmjslave1:/usr/local
scp -r /usr/local/elasticsearch/ xmjslave2:/usr/local
~~~

~~~properties
4)添加es用户，es默认root用户无法启动，需要改为其他用户
useradd estest 
#修改密码 
passwd estest

#改变es目录拥有者账号
chown -R estest /usr/local/elasticsearch/
~~~

~~~properties
5)修改sysctl.conf
vim /etc/sysctl.conf

#末尾添加(执行sysctl -p 让其生效)：
vm.max_map_count=655360
~~~

~~~properties
6)修改limits.conf
vim /etc/security/limits.conf

#末尾添加:
* soft nofile 65536 
* hard nofile 65536 
* soft nproc 4096 
* hard nproc 4096
~~~

**4.启动es**

~~~properties
su estest
/usr/local/elasticsearch/bin/elasticsearch
~~~

**5.验证集群**

~~~properties
http://xmjmaster:9200/_cat/health?v
~~~

![image-20200923142920035](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200923142920035.png)

**二、安装elasticsearch head插件**

**1.nodejs安装**

~~~properties
cd /usr/local
wget https://nodejs.org/dist/v10.15.3/node-v10.15.3-linux-x64.tar.xz
tar xf node-v10.15.3-linux-x64.tar.xz
cd node-v10.15.3-linux-x64/
./bin/node -v
~~~

~~~properties
#设置软连接
ln -s /usr/local/node-v10.15.3-linux-x64/bin/npm /usr/local/bin/ 
ln -s /usr/local/node-v10.15.3-linux-x64/bin/node /usr/local/bin/
~~~

**2.phantomjs安装配置**

~~~properties
1)下载解压缩
cd /usr/local
wget https://github.com/Medium/phantomjs/releases/download/v2.1.1/phantomjs-2.1.1-linux-x86_64.tar.bz2
#注意安装
yum install -y bzip2
tar -jxvf phantomjs-2.1.1-linux-x86_64.tar.bz2
~~~

~~~properties
2）编辑环境变量
vim /etc/profile
export PATH=$PATH:/usr/local/phantomjs-2.1.1-linux-x86_64/bin
source /etc/profile
~~~

**3.elasticsearch-head安装**

~~~properties
npm install -g grunt-cli 
npm install grunt 
npm install grunt-contrib-clean 
npm install grunt-contrib-concat 
npm install grunt-contrib-watch 
npm install grunt-contrib-connect

yum -y install git
git clone git://github.com/mobz/elasticsearch-head.git 
cd elasticsearch-head 
npm install -g cnpm --registry=https://registry.npm.taobao.org
~~~

**4.启动**

~~~properties
npm run start
~~~

**5.测试**

~~~properties
http://xmjmaster:9100/
~~~

![image-20200923153244367](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200923153244367.png)

