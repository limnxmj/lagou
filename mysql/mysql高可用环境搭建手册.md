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

​			rpm -qa|grep -i mariadb

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
		relay_log=mysql-relay-bin
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
		log_bin=mysql-bin
		relay_log=mysql-relay-bin
		read_only=1
		relay_log_purge=0
		
		#对哪些库不同步
		binlog-ignore-db=information_schema
		binlog-ignore-db=performance_schema
		binlog-ignore-db=sys
	
		#对哪些库同步
		#binlog-do-db=lagou

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



**四、MHA安装及配置（MHA: 192.168.238.159）**

​	1.各节点（mysql一主两从及MHA）yum源配置

		1)
		wget -O /etc/yum.repos.d/CentOS6-Base-163.repo http://mirrors.163.com/.help/CentOS6-Base-163.repo
		sed -i 's/$releasever/6/g' /etc/yum.repos.d/CentOS6-Base-163.repo
		wget -O /etc/yum.repos.d/CentOS-Base-Ali.repo http://mirrors.aliyun.com/repo/Centos-6.repo
		sed -i 's/$releasever/6/g' /etc/yum.repos.d/CentOS-Base-Ali.repo
		2)vim /etc/yum.repos.d/epel.repo
		[epel]
		name=epel
		baseurl=https://mirrors.ustc.edu.cn/epel/6Server/x86_64/
		gpgcheck=0
		enabled=1
		3)yum groupinstall 'Development tools' 'Server Platform Development' -y
​	2.配置各节点（mysql一主两从及MHA）免密码通信

		1)各server192.168.238.156-159
			mkdir -pv /root/.ssh
	  		mkdir -pv /root/rpms
		2)MHA server(192.168.238.159)
			ssh-keygen -t rsa -P ''  ##执行命令后直接回车，生成文件名默认
			cat .ssh/id_rsa.pub > .ssh/authorized_keys
			chmod 600 .ssh/authorized_keys
			scp -p .ssh/id_rsa .ssh/authorized_keys 192.168.238.156:/root/.ssh
			scp -p .ssh/id_rsa .ssh/authorized_keys 192.168.238.157:/root/.ssh
			scp -p .ssh/id_rsa .ssh/authorized_keys 192.168.238.158:/root/.ssh
			ssh 192.168.238.156 'ip addr list'
			ssh 192.168.238.157 'ip addr list'
			ssh 192.168.238.158 'ip addr list'
			ssh 192.168.238.159 'ip addr list'

​	3.安装mha4mysql-manager和mha4mysql-node

	管理端安装(MHA:192.168.238.159)
		1)安装MHA依赖包
			yum install -y perl-DBD-MySQL
			yum install -y perl-Config-Tiny
			yum install -y perl-Log-Dispatch
			yum install -y perl-Parallel-ForkManager
			yum install -y ca-certificates
			yum install -y perl-Parallel-ForkManager
			yum install -y rrdtool perl-rrdtool rrdtool-devel perl-Params-Validate
			yum install -y perl-Time-HiRes
	
		2)安装mha4mysql-manager和mha4mysql-node
			rpm -ivh mha4mysql-manager-0.56-0.el6.noarch.rpm --nodeps --force
			rpm -ivh mha4mysql-node-0.56-0.el6.noarch.rpm --nodeps --force
	客户端安装(192.168.238.156-158)
		yum install -y perl-DBD-MySQL
		rpm -ivh mha4mysql-node-0.56-0.el6.noarch.rpm --nodeps --force

​	4.创建MHA的工作目录和日志目录(MHA:192.168.238.159)

	mkdir -pv /dbdata/masterha/app1
	mkdir -pv /etc/masterha

​	5.vim /etc/masterha/app1.cnf(MHA:192.168.238.159)

	[server default]
	user=mhauser
	password=mhapass
	manager_workdir=/dbdata/masterha/app1
	manager_log=/dbdata/masterha/app1/manager.log
	remote_workdir=/dbdata/masterha/app1
	ssh_user=root
	repl_user=repluser
	repl_password=replpass
	ping_interval=1
	[server1]
	hostname=192.168.238.156
	candidate_master=1
	#ssh_port=22
	[server2]
	hostname=192.168.238.157
	candidate_master=1
	[server3]
	hostname=192.168.238.158
	#no_master=1

​	6.masterha_check

	masterha_check_ssh --conf=/etc/masterha/app1.cnf
	masterha_check_repl --conf=/etc/masterha/app1.cnf

​	![image-20200616224514338](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200616224514338.png)

![image-20200616232800854](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200616232800854.png)

​	7.masterha_manager --conf=/etc/masterha/app1.cnf



**五、测试MHA**

1.主节点(192.168.238.156)关闭mysql

​	service mysql

2.从节点(192.168.238.157)查看slave状态和master状态

show slave status \G

![image-20200616234423500](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200616234423500.png)

3.从节点(192.168.238.158)查看slave状态

show slave status \G

![image-20200616234404252](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200616234404252.png)

4.原主库启动mysql, 登录后切换为从库

​	change master to master_host='192.168.238.157',master_user='repluser',master_password='replpass',master_log_file='mysql-bin.000003',master_log_pos=718;

​	start slave;
​	show slave status \G

5.测试节点(192.168.238.157)插入sql

	insert into position (name, salary, city) values ('lm', '10', '北京');
	insert into position_detail(pid, description) values (11111, 'haha');
	
	测试插入效果
	select * from position;
	select * from position_detail;

**六、问题记录**

**1.yum提示problem making ssl connection错误**

​	执行命令：yum install -y ca-certificates

​	然后重新执行报错命令

**2.Host '192.168.238.159' is not allowed to connect to this MySQL server, but this is not a MySQL crash. Check MySQL server settings**

​	mysql服务器授权

​	grant all on *.* to 'mhauser'@'192.168.238.%' identified by 'mhapass';

​	grant all on *.* to 'repluser'@'192.168.238.%' identified by 'replpass';

​	flush privileges;

**3.[error][/usr/share/perl5/vendor_perl/MHA/ServerManager.pm, ln188] There is no alive server. We can't do failover**

​	alter user 'mhauser'@'192.168.238.%' identified with mysql_native_password by 'mhapass';

​	flush privileges;

**4.Binlog filtering check failed on 192.168.238.157(192.168.238.157:3306)! All log-bin enabled servers must have same binlog filtering rules (same binlog-do-db and binlog-ignore-db). Check SHOW MASTER STATUS output and set my.cnf correctly**

​	修改从库配置  vim /etc/my.cnf

	#对哪些库不同步
	binlog-ignore-db=information_schema
	binlog-ignore-db=performance_schema
	binlog-ignore-db=sys
	
	#对哪些库同步
	#binlog-do-db=lagou
**5.原主库(192.168.238.156)改为从库后，测试新主库(192.168.238.157)插入数据，原主库无数据**

master_user和master_password错误：改为MHA中配置的repluser和replpass

change master to master_host='192.168.238.157',master_user='repluser',master_password='replpass',master_log_file='mysql-bin.000003',master_log_pos=718;