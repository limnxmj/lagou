**一、环境**

​	1.linux版本：CentOS release 6.8 (Final)

​	2.mysql版本：mysql-5.7.30-1.el6.x86_64.rpm-bundle.tar

​	3.架构

​		4台主机，1主、2从、1 MHA

​		master:192.168.238.156

​		slave: 192.168.238.157, 192.168.238.158

​		MHA: 192.168.238.159

**二、mysql安装**

​	1.移除原系统mysql和mariadb

​		1)查看系统是否安装mariadb和mysql

​			mariadbrpm -qa|grep -i mariadb

​			rpm -qa|grep -i mysql

​		2)移除

		rpm -e MySQL-devel-5.6.34-1.el6.i686 --nodeps
		rpm -e MySQL-server-5.6.34-1.el6.i686 --nodeps
		rpm -e MySQL-client-5.6.34-1.el6.i686 --nodeps
​	2.rpm方式安装mysql

​		1)解压缩mysql tar包

		tar -xvf mysql-5.7.30-1.el6.x86_64.rpm-bundle.tar
​		2)依次安装common/libs/libs-compat/client/server/devel rpm

		rpm -ivh mysql-community-common-5.7.30-1.el6.x86_64.rpm
		rpm -ivh mysql-community-libs-5.7.30-1.el6.x86_64.rpm
		rpm -ivh mysql-community-libs-compat-5.7.30-1.el6.x86_64.rpm
		rpm -ivh mysql-community-client-5.7.30-1.el6.x86_64.rpm
		rpm -ivh mysql-community-server-5.7.30-1.el6.x86_64.rpm
		rpm -ivh mysql-community-devel-5.7.30-1.el6.x86_64.rpm

​		3)初始化mysql

​			mysqld --initialize --user=mysql

​		4)查看mysql初始化密码

​			cat /var/log/mysqld.log

​		5)启动mysql

​			service mysql start

​		6)登录重置密码
​			set password=password('root');
​		7)重新登录

**三、mysql主从环境搭建**

​	**1.主库配置（master:192.168.238.156）**

​		1)修改 /etc/my.cnf

​			vim /etc/my.cnf

		[mysqld]
		log_bin=mysql-bin
		server-id=1
		sync-binlog=1
	
		#对哪些库不同步
		binlog-ignore-db=information_schema
		binlog-ignore-db=performance_schema
		binlog-ignore-db=sys
	
		#对哪些库同步
		#binlog-do-db=lagou
​		2)重启mysql

​			service mysqld restart

​		3)登录mysql授权

		grant replication slave on *.* to 'root'@'%' identified by 'root';
		grant all privileges on *.* to 'root'@'%' identified by 'root';
		flush privileges;
​		4)查看主库状态

​			show master status

![image-20200614161242260](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200614161242260.png)

​	**2.从库配置（slave: 192.168.238.157）**

​		1)修改 /etc/my.cnf

​			vim /etc/my.cnf

		[mysqld]
		server-id=2
		relay_log=mysql-relay-bin
		##只读配置
		#read_only=1

​		2)重启mysql

​			service mysqld restart

​		3)登录mysql查看从库状态
​			show slave status \G

​		4)同步主库
​			change master to master_host='192.168.238.156',master_port=3306,master_user='root',master_password='root',master_log_file='mysql-bin.000001',master_log_pos=869;

​		5)开启从库
​			start slave;
​		6)查看从库状态
​			show slave status \G

![image-20200614161846329](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200614161846329.png)

​	**3.从库配置（slave: 192.168.238.158）**

同从库(192.168.238.157)配置， /etc/my.cnf 中server-id=3

​	

​	**4.测试主从配置**

​	主库执行sql，查看从库是否同步

	create database lagou;
	show database;
	use lagou;
	create table position (id int primary key auto_increment, name varchar(20), salary varchar(20), city varchar(20)) engine=innodb charset=utf8;
	create table position_detail (id int primary key auto_increment, pid int, description text) engine=innodb charset=utf8;
![image-20200614162606887](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200614162606887.png)

![image-20200614162635450](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200614162635450.png)

![image-20200614162658372](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200614162658372.png)

​	**5.半同步复制机制配置**

​		**主库配置**

​		1)登录mysql后查看是否有动态加载
​			select @@have_dynamic_loading;

​		2)查看插件
​			show plugins;

​		3)安装插件
​			install plugin rpl_semi_sync_master soname 'semisync_master.so';

​		4)查看
​			show variables like '%semi%';

​		5)设置rpl_semi_sync_master_enabled和rpl_semi_sync_master_timeout
​			set global rpl_semi_sync_master_enabled=1;
​			set global rpl_semi_sync_master_timeout=1000;

![image-20200614164331586](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200614164331586.png)

**从库配置**

​	1)安装插件：install plugin rpl_semi_sync_slave soname 'semisync_slave.so';

​	2)设置rpl_semi_sync_slave_enabled

​		set global rpl_semi_sync_slave_enabled=1;

​	3)重启slave

​		stop slave;

​		start slave;

![image-20200614164235716](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200614164235716.png)



**测试半同步复制**

​	1主库执行sql，查看从库是否同步

	insert into position (name, salary, city) values ('lm', '10', '北京');
	insert into position_detail(pid, description) values (1, 'haha');
​	查看log日志：tail -200 /var/log/mysqld.log

![image-20200614165318788](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200614165318788.png)