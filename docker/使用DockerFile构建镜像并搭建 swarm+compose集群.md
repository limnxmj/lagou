**一.环境**

~~~
centos7:
192.168.238.160(xmjmaster)
192.168.238.161(xmjslave1)
~~~

**二.docker安装**

**1.卸载历史版本**

~~~bash
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

~~~bash
yum install -y yum-utils
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

cd /etc/yum.repos.d
ll
~~~

**3.安装Docker引擎**

~~~bash
yum install -y docker-ce docker-ce-cli containerd.io
~~~

**4.启动docker**

~~~bash
#开机启动 
systemctl enable docker 
#启动 
systemctl start docker 
#查看Docker状态 
docker info
~~~

**三.Swarm集群安装**

**1.下载镜像**

~~~bash
# 拉取镜像 
docker pull swarm
~~~

**2.创建新集群**

~~~bash
[root@xmjmaster yum.repos.d]# docker swarm init --advertise-addr 192.168.238.160
~~~

![image-20201007232310150](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20201007232310150.png)

**3.添加工作节点到集群**

~~~bash
[root@xmjslave1 yum.repos.d]# docker swarm join --token SWMTKN-1-2trra1xu8vdr5crmfojr5ya3o6hs7me7nidpidubyyyip9z66t-apjvo86nyilchgmr4x59j99kb 192.168.238.160:2377
~~~

![image-20201007232248684](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20201007232248684.png)

**4.查看集群状态和节点信息**

~~~bash
#查看集群状态
docker info
#查看节点信息 
docker node ls
~~~

![image-20201007232505255](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20201007232505255.png)

**四.Compose的安装**

**1.下载Docker Compose的当前稳定版本**

~~~bash
curl -L "https://github.com/docker/compose/releases/download/1.26.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
~~~

**2.将可执行权限应用于二进制文件**

~~~bash
chmod +x /usr/local/bin/docker-compose
~~~

**3.添加到环境中**

~~~bash
#ln -s ： 软链接 
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
~~~

**4.测试安装**

~~~bash
docker-compose --version
~~~

![image-20201008000329122](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20201008000329122.png)



**五.swarm和compose整合**

**1.编写docker-compose.yml**

~~~yml
version: "3.0"
services:
  nginx:
    image: nginx:1.18.0
    environment:
      - TZ=Asia/beijing
    ports:
      - 80:80
      - 443:443
    volumes:
      - /docker/nginx/log:/var/log/nginx
      - /docker/nginx/www:/etc/nginx/html
      - /etc/letsencrypt:/etc/letsencrypt
    deploy:
      mode: replicated
      replicas: 2
  mysql:
    image: mysql:5.7.30 
    ports:
      - 13306:3306
    command:
      --default-authentication-plugin=mysql_native_password
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
      --explicit_defaults_for_timestamp=true
      --lower_case_table_names=1
      --default-time-zone=+8:00 
    environment:
      MYSQL_ROOT_PASSWORD: "123456"
    volumes:
       - "/docker/mysql/db:/var/lib/mysql"
    deploy:
      mode: replicated
      replicas: 2
  redis:
    image: redis:5.0.9
    environment:
      - TZ=Asia/beijing
    ports:
      - 6379:6379
    volumes:
      - /docker/redis/data:/data
    deploy: 
      mode: replicated 
      replicas: 2
~~~

~~~bash
mkdir -p /docker/nginx/log
mkdir -p /docker/nginx/www
mkdir -p /etc/letsencrypt
mkdir -p /docker/mysql/db
mkdir -p /docker/redis/data
~~~

**2.运行**

~~~bash
docker stack deploy -c docker-compose.yml web
~~~

**3.查看服务**

~~~bash
docker stack services web
~~~

![image-20201008083558268](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20201008083558268.png)

**五.利用dockerfile将Hot.jar构建成镜像lgedu/hot:1.0** 

**1.编写DockerFile**

~~~bash
vim DockerFile-hot

FROM java:openjdk-8-alpine
ADD hot-0.0.1-SNAPSHOT.jar hot.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/hot.jar"]
~~~

**2.创建镜像/生成镜像的标签信息**

~~~bash
docker build -f /usr/local/dockerfiles/DockerFile-hot -t lgedu/hot:1.0 .
~~~

![image-20201008161042221](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20201008161042221.png)

**3.修改docker-compose.yml**

~~~bash
##追加
  hot:
    image: lgedu/hot:1.0
    ports:
      - 8080:8080
    deploy: 
      mode: replicated 
      replicas: 2
~~~

**4.将镜像导出上传到xmjslave1,并在xmjslave1导入hot镜像**

~~~
docker save lgedu/hot > /usr/local/dockerfiles/hot.tar
scp hot.tar xmjslave1:/usr/local/dockerfiles/
docker load -i hot.tar 
~~~

**5.运行**

~~~
docker stack deploy -c docker-compose.yml web
~~~

**6.查看服务**

~~~
docker stack services web
~~~

![image-20201008162752793](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20201008162752793.png)

**7.浏览器访问hot**

~~~
http://xmjmaster:8080/hello
http://xmjslave1:8080/hello
~~~

![image-20201008162833312](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20201008162833312.png)

![image-20201008162852299](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20201008162852299.png)