**方式一**

**1.下载solr项目包**

~~~
wget https://mirror.bit.edu.cn/apache/lucene/solr/7.7.3/solr-7.7.3.tgz
~~~

**2.解压缩**

~~~
tar -xvf solr-7.7.3.tgz
~~~

**3.启动solr**

~~~
cd /root/solr/solr-7.7.3/bin

##-port 默认端口 8983
./solr start -force
~~~

**4.使用浏览器访问**

~~~
http://xmj:8983
~~~

![image-20200918141901897](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200918141901897.png)

**5.配置solr_core**

~~~
cp /root/solr/solr-7.7.3/server/solr/configsets/_default/conf/ -rf /root/solr/solr-7.7.3/server/solr/new_core/
mv managed-schema schema.xml
~~~

![image-20200918142305971](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200918142305971.png)

**方式二**

**1.下载tomcat**

~~~
wget https://mirrors.tuna.tsinghua.edu.cn/apache/tomcat/tomcat-8/v8.5.58/bin/apache-tomcat-8.5.58.tar.gz

tar -zxvf apache-tomcat-8.5.58.tar.gz
~~~

**2.复制server/solr-webapp到tomcat的webapps目录下并改名为solr**

~~~
cd /root/solr/solr-7.7.3/server/solr-webapp/
cp -r webapp/ /root/solr/apache-tomcat-8.5.58/webapps
cd /root/solr/apache-tomcat-8.5.58/webapps
mv webapp solr
~~~

**3.复制所需依赖jar包**

~~~
cp /root/solr/solr-7.7.3/server/lib/ext/* /root/solr/apache-tomcat-8.5.58/webapps/solr/WEB-INF/lib/

cp /root/solr/solr-7.7.3/server/lib/metrics-* /root/solr/apache-tomcat-8.5.58/webapps/solr/WEB-INF/lib/
~~~

**4.配置solrhome**

~~~
mkdir -p /usr/local/solr/solrhome
cp -r /root/solr/solr-7.7.3/server/solr/* /usr/local/solr/solrhome/
~~~



**5.配置Tomcat**

~~~
cd /root/solr/apache-tomcat-8.5.58/webapps/solr/WEB-INF
vim web.xml

<!-- 修改solrhome路径 -->
<env-entry>
    <env-entry-name>solr/home</env-entry-name>
    <env-entry-value>/usr/local/solr/solrhome</env-entry-value>
    <env-entry-type>java.lang.String</env-entry-type>
</env-entry>

<!-- 去掉安全认证 -->
<!--
    <security-constraint>
    ...
    </security-constraint> 
-->
~~~

**6.启动tomcat**

~~~
cd /root/solr/apache-tomcat-8.5.58/bin/
./startup.sh
~~~

**7.在浏览器访问solr**

~~~
http://xmj:8080/solr/index.html
~~~

**8.配置solr_core**

~~~
cp /root/solr/solr-7.7.3/server/solr/configsets/_default/conf/ -rf /usr/local/solr/solrhome/new_core/
mv managed-schema schema.xml
~~~

![image-20200918145147391](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200918145147391.png)

