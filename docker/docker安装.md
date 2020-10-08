**1.卸载历史版本**

~~~properties
#查看安装 
yum list installed | grep docker 
#卸载 
yum -y remove containerd.io.x86_64 
yum -y remove docker-ce.x86_64 
yum -y remove docker-ce-cli.x86_64 
#删库 
rm -rf /var/lib/docker
~~~

**2.安装官方yum源**

~~~properties
yum install -y yum-utils
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

cd /etc/yum.repos.d
ll
~~~

**3.安装Docker引擎**

~~~properties
yum install -y docker-ce docker-ce-cli containerd.io
~~~

**4.启动docker**

~~~properties
#开机启动 
systemctl enable docker 
#启动 
systemctl start docker 
#查看Docker状态 
docker info
~~~

