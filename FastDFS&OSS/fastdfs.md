**一、环境**

~~~
linux: centos-release-7-5.1804.el7.centos.x86_64
fastdfs：group1:192.168.238.160
~~~

**二、安装**

**1.安装编译环境**

~~~
yum install git gcc gcc-c++ make automake vim wget libevent -y
~~~

**2. 安装libfastcommon 基础库**

~~~
mkdir /root/fastdfs
cd /root/fastdfs/
git clone https://github.com/happyfish100/libfastcommon.git --depth 1
cd libfastcommon/
./make.sh && ./make.sh install
~~~

**3.安装fastdfs**

~~~
cd /root/fastdfs/
wget https://github.com/happyfish100/fastdfs/archive/V5.11.tar.gz
tar -zxvf V5.11.tar.gz
cd fastdfs-5.11
./make.sh && ./make.sh install

~~~

**4.修改配置**

~~~
#配置文件准备 
cp /etc/fdfs/tracker.conf.sample /etc/fdfs/tracker.conf 
cp /etc/fdfs/storage.conf.sample /etc/fdfs/storage.conf 
cp /etc/fdfs/client.conf.sample /etc/fdfs/client.conf 
cp /root/fastdfs/fastdfs-5.11/conf/http.conf /etc/fdfs 
cp /root/fastdfs/fastdfs-5.11/conf/mime.types /etc/fdfs
~~~

~~~
vim /etc/fdfs/tracker.conf 
#需要修改的内容如下 
port=22122
base_path=/home/fastdfs
~~~

~~~
vim /etc/fdfs/storage.conf 
#需要修改的内容如下 
port=23000
group_name=group1
base_path=/home/fastdfs
# 数据和日志文件存储根目录
store_path0=/home/fastdfs
# 第一个存储目录 tracker_server=192.168.238.160:22122
# http访问文件的端口(默认8888,看情况修改,和nginx中保持一致)
http.server_port=8888
~~~

**5.启动**

~~~
mkdir /home/fastdfs -p
/usr/bin/fdfs_trackerd /etc/fdfs/tracker.conf restart
/usr/bin/fdfs_storaged /etc/fdfs/storage.conf restart
查看所有运行的端口
netstat -ntlp
~~~

**6.安装fastdfs-nginx-module**

~~~
cd /root/fastdfs 
wget https://github.com/happyfish100/fastdfs-nginx-module/archive/V1.20.tar.gz 
##解压
tar -zxvf V1.20.tar.gz 
cd fastdfs-nginx-module-1.20/src 
vim config 
##修改第5行和15行 修改成 
ngx_module_incs="/usr/include/fastdfs /usr/include/fastcommon/"
CORE_INCS="$CORE_INCS /usr/include/fastdfs /usr/include/fastcommon/"
~~~

~~~
cp mod_fastdfs.conf /etc/fdfs/

vim /etc/fdfs/mod_fastdfs.conf
#需要修改的内容如下
tracker_server=192.168.238.160:22122
url_have_group_name=true
store_path0=/home/fastdfs
~~~

~~~
mkdir -p /var/temp/nginx/client
~~~

**7.安装nginx**

~~~
cd /root/fastdfs
wget http://nginx.org/download/nginx-1.15.6.tar.gz
tar -zxvf nginx-1.15.6.tar.gz
cd nginx-1.15.6/
~~~

~~~
yum -y install pcre-devel openssl openssl-devel
# 添加fastdfs-nginx-module模块
./configure --add-module=/root/fastdfs/fastdfs-nginx-module-1.20/src
#编译安装
make && make install
#查看模块是否安装上
/usr/local/nginx/sbin/nginx -V
~~~

~~~
vim /usr/local/nginx/conf/nginx.conf

#添加如下配置
server {
    listen 8888; 
    server_name localhost; 
    location ~/group[0-9]/ { 
        ngx_fastdfs_module; 
    } 
}
~~~

~~~
/usr/local/nginx/sbin/nginx
~~~



**三、测试**

**1.测试上传**

~~~
vim /etc/fdfs/client.conf
#需要修改的内容如下
base_path=/home/fastdfs
#tracker服务器IP和端口 
tracker_server=192.168.238.160:22122 
#保存后测试,返回ID表示成功 如：group1/M00/00/00/xxx.png 
/usr/bin/fdfs_upload_file /etc/fdfs/client.conf /root/fastdfs/1.png

~~~

![image-20200715014653502](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200715014653502.png)

**2.测试http下载**

~~~
http://192.168.238.160:8888/group1/M00/00/00/wKjuoF8N73KAWKqOAABDL5pi8X4134.png
~~~

![image-20200715020515379](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200715020515379.png)

**四、GraphicsMagick生成缩略图**

**1.编译安装-LuaJIT**

~~~
wget http://luajit.org/download/LuaJIT-2.0.4.tar.gz
tar -zxvf LuaJIT-2.0.4.tar.gz
mv LuaJIT-2.0.4 /usr/local
cd /usr/local/LuaJIT-2.0.4/
make && make install
~~~

~~~
vim /etc/profile
export LUAJIT_LIB=/usr/local/lib
export LUAJIT_INC=/usr/local/include/luajit-2.0
source /etc/profile
~~~

~~~
ln -s /usr/local/lib/libluajit-5.1.so.2 /lib64/libluajit-5.1.so.2
~~~

**2.编译安装-Lua**

~~~
wget http://www.lua.org/ftp/lua-5.3.3.tar.gz
tar -zxvf lua-5.3.3.tar.gz 
mv lua-5.3.3 /usr/local
cd /usr/local/lua-5.3.3/
make linux && make install
~~~

**3.编译安装-GraphicsMagick**

~~~
#安装graphicsmagick支持的图片格式
yum install libjpeg libjpeg-devel libpng libpng-devel giflib giflib-devel freetype freetype-devel -y
~~~

~~~
##安装-GraphicsMagick
wget https://sourceforge.net/projects/graphicsmagick/files/graphicsmagick/1.3.35/GraphicsMagick-1.3.35.tar.gz
tar -zxvf GraphicsMagick-1.3.35.tar.gz 
cd GraphicsMagick-1.3.35/
./configure --prefix=/usr/local/GraphicsMagick --enable-shared
make && make install
~~~

~~~
/usr/local/GraphicsMagick/bin/gm version
~~~



**4.编译安装-nginx**

~~~
#先查看nginx编译安装时安装了哪些模块
/usr/local/nginx/sbin/nginx -V

nginx version: nginx/1.15.6
built by gcc 4.8.5 20150623 (Red Hat 4.8.5-39) (GCC) 
configure arguments: --add-module=/root/fastdfs/fastdfs-nginx-module-1.20/src
~~~

~~~
mv ngx_devel_kit/ /usr/local
tar -zxvf zlib-1.2.11.tar.gz 
mv zlib-1.2.11 /usr/local
tar -zxvf v0.10.9rc7.tar.gz 
mv lua-nginx-module-0.10.9rc7/ /usr/local
~~~

~~~
#在nginx的源码目录下，通过–add-module=xxx的方式，追加模块
./configure --add-module=/root/fastdfs/fastdfs-nginx-module-1.20/src --prefix=/usr/local/nginx --with-zlib=/usr/local/zlib-1.2.11 --add-module=/usr/local/lua-nginx-module-0.10.9rc7 --add-module=/usr/local/ngx_devel_kit

make && make install
/usr/local/nginx/sbin/nginx -V
~~~

**5.nginx配置**

~~~
vim /usr/local/nginx/conf/nginx.conf
server {
    listen       8888;
    server_name  localhost;    

    location ~/group[0-9]/ {
        ngx_fastdfs_module;
    }

    location /lua {
        default_type 'text/plain';
        content_by_lua 'ngx.say("Hello, Lua!")';
    }

    location / {
        root   /resource/image;            
        index  index.html index.htm;
    }

    location ~* ^((.+\.(jpg|jpeg|gif|png))_(\d+)x(\d+)\.(jpg|jpeg|gif|png))$ {
        root /resource/image;
        if (!-f $request_filename) {
            add_header X-Powered-By 'Lua GraphicsMagick';
            add_header file-path $request_filename;
            set $request_filepath /resource/image$2;
            set $width $4;
            set $height $5;
            set $ext $6;
            content_by_lua_file /usr/local/nginx/lua/ImageResizer.lua;
        }            
    }
}
~~~

~~~
mkdir /resource/image -p
chmod 777 -R /resource/image/
mkdir /usr/local/nginx/lua -p
~~~

~~~
vim /usr/local/nginx/lua/ImageResizer.lua

#添加如下配置
local command = "/usr/local/GraphicsMagick/bin/gm convert   -auto-orient-strip " .. ngx.var.request_filepath .. " -resize " .. ngx.var.width .. "x" .. ngx.var.height .. " +profile \"*\" " .. ngx.var.request_filepath .. "_" .. ngx.var.width .. "x" .. ngx.var.height .. "." .. ngx.var.ext;
os.execute(command);    
ngx.exec(ngx.var.request_uri);
~~~

~~~
/usr/local/nginx/sbin/nginx -s reload
##访问测试
http://192.168.238.160:8888/lua
~~~

![image-20200715091220265](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200715091220265.png)

~~~
http://192.168.238.160:8888/1.png
http://192.168.238.160:8888/1.png_100x100.png
~~~

![image-20200715094421435](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200715094421435.png)



![image-20200715094350712](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200715094350712.png)

**安装过程遇到的问题**

**1.编译安装Lua出现：lua.c:80:31: fatal error: readline/readline.h: No such file or directory**

解决：

~~~
yum install readline-devel
~~~

