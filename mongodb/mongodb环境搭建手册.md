**一、环境**

​	1.linux版本：CentOS release 6.8 (Final)

​	2.mongodb版本：mongodb-linux-x86_64-4.1.3.tgz

​	3.架构

​		主机IP:192.168.238.153

​		路由节点端口：27017

​		配置节点集群：17011,17013,17015

​		分片节点1：37011,37013,37015,37017

​		分片节点2：47011,47013,47015,47017

​		分片节点3：57011,57013,57015,57017

​		分片节点4：58011,58013,58015,58017

**二、生成秘钥文件 并修改权限**

```
openssl rand -base64 756 > /usr/local/mongo/mongodb_shard_cluster/key/keyFile.file
chmod 600 key/keyFile.file
```

**三、配置并启动config节点集群**

**1.节点config-17011.conf**

```
# 数据库文件位置
dbpath=config/config1
#日志文件位置
logpath=config/logs/config1.log
# 以追加方式写入日志
logappend=true
# 是否以守护进程方式运行
fork = true
bind_ip=0.0.0.0
port = 17011
# 表示是一个配置服务器
configsvr=true
#配置服务器副本集名称
replSet=configsvr
auth=true
keyFile=key/keyFile.file
```

**2.节点config-17013.conf**

```
# 数据库文件位置
dbpath=config/config2
#日志文件位置
logpath=config/logs/config2.log
# 以追加方式写入日志
logappend=true
# 是否以守护进程方式运行
fork = true
bind_ip=0.0.0.0
port = 17013
# 表示是一个配置服务器
configsvr=true
#配置服务器副本集名称
replSet=configsvr
auth=true
keyFile=key/keyFile.file
```

**3.节点config-17015.conf**

```
# 数据库文件位置
dbpath=config/config3
#日志文件位置
logpath=config/logs/config3.log
# 以追加方式写入日志
logappend=true
# 是否以守护进程方式运行
fork = true
bind_ip=0.0.0.0
port = 17015
# 表示是一个配置服务器
configsvr=true
#配置服务器副本集名称
replSet=configsvr
auth=true
keyFile=key/keyFile.file
```

**4.启动配置节点**

```
./bin/mongod -f config/config-17011.conf 
./bin/mongod -f config/config-17013.conf 
./bin/mongod -f config/config-17015.conf 
```

**5.进入节点并配置集群**

```
./bin/mongo --port 17011
use admin
var cfg = {"_id":"configsvr", 
    "members":[ 
        {"_id":1,"host":"192.168.238.153:17011"}, 
        {"_id":2,"host":"192.168.238.153:17013"}, 
        {"_id":3,"host":"192.168.238.153:17015"}
    ] 
};
rs.initiate(cfg)
```

![image-20200706061235563](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200706061235563.png)

**四、配置shard集群**

**1.shard1(37011,37013,37015,37017)**

```
--shard1-37011.conf
dbpath=shard/shard1/shard1-37011
bind_ip=0.0.0.0
port=37011
fork=true
logpath=shard/shard1/logs/shard1-37011.log
replSet=shard1
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard1-37013.conf
dbpath=shard/shard1/shard1-37013
bind_ip=0.0.0.0
port=37013
fork=true
logpath=shard/shard1/logs/shard1-37013.log
replSet=shard1
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard1-37015.conf
dbpath=shard/shard1/shard1-37015
bind_ip=0.0.0.0
port=37015
fork=true
logpath=shard/shard1/logs/shard1-37015.log
replSet=shard1
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard1-37017.conf
dbpath=shard/shard1/shard1-37017
bind_ip=0.0.0.0
port=37017
fork=true
logpath=shard/shard1/logs/shard1-37017.log
replSet=shard1
shardsvr=true
auth=true
keyFile=key/keyFile.file

--启动节点
./bin/mongod -f shard/shard1/shard1-37011.conf
./bin/mongod -f shard/shard1/shard1-37013.conf
./bin/mongod -f shard/shard1/shard1-37015.conf
./bin/mongod -f shard/shard1/shard1-37017.conf

--配置节点集群
./bin/mongo --port 37011
var cfg = {"_id":"shard1", 
    "protocolVersion" : 1, 
    "members":[ 
        {"_id":1,"host":"192.168.238.153:37011"}, 
        {"_id":2,"host":"192.168.238.153:37013"}, 
        {"_id":3,"host":"192.168.238.153:37015"}, 
        {"_id":4,"host":"192.168.238.153:37017","arbiterOnly":true}
    ]
};
rs.initiate(cfg)
```

![image-20200706063504593](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200706063504593.png)

**2.shard2(47011,47013,47015,47017)**

```
--shard2-47011.conf
dbpath=shard/shard2/shard2-47011
bind_ip=0.0.0.0
port=47011
fork=true
logpath=shard/shard2/logs/shard2-47011.log
replSet=shard2
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard2-47013.conf
dbpath=shard/shard2/shard2-47013
bind_ip=0.0.0.0
port=47013
fork=true
logpath=shard/shard2/logs/shard2-47013.log
replSet=shard2
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard2-47015.conf
dbpath=shard/shard2/shard2-47015
bind_ip=0.0.0.0
port=47015
fork=true
logpath=shard/shard2/logs/shard2-47015.log
replSet=shard2
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard2-47017.conf
dbpath=shard/shard2/shard2-47017
bind_ip=0.0.0.0
port=47017
fork=true
logpath=shard/shard2/logs/shard2-47017.log
replSet=shard2
shardsvr=true
auth=true
keyFile=key/keyFile.file

--启动节点
./bin/mongod -f shard/shard2/shard2-47011.conf
./bin/mongod -f shard/shard2/shard2-47013.conf
./bin/mongod -f shard/shard2/shard2-47015.conf
./bin/mongod -f shard/shard2/shard2-47017.conf

--配置节点集群
./bin/mongo --port 47011
var cfg = {"_id":"shard2", 
    "protocolVersion" : 1, 
    "members":[ 
        {"_id":1,"host":"192.168.238.153:47011"}, 
        {"_id":2,"host":"192.168.238.153:47013"}, 
        {"_id":3,"host":"192.168.238.153:47015"}, 
        {"_id":4,"host":"192.168.238.153:47017","arbiterOnly":true}
    ]
};
rs.initiate(cfg)
```

![image-20200706064550835](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200706064550835.png)

**3.shard3(57011,57013,57015,57017)**

```
--shard3-57011.conf
dbpath=shard/shard3/shard3-57011
bind_ip=0.0.0.0
port=57011
fork=true
logpath=shard/shard3/logs/shard3-57011.log
replSet=shard3
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard3-57013.conf
dbpath=shard/shard3/shard3-57013
bind_ip=0.0.0.0
port=57013
fork=true
logpath=shard/shard3/logs/shard3-57013.log
replSet=shard3
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard3-57015.conf
dbpath=shard/shard3/shard3-57015
bind_ip=0.0.0.0
port=57015
fork=true
logpath=shard/shard3/logs/shard3-57015.log
replSet=shard3
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard3-57017.conf
dbpath=shard/shard3/shard3-57017
bind_ip=0.0.0.0
port=57017
fork=true
logpath=shard/shard3/logs/shard3-57017.log
replSet=shard3
shardsvr=true
auth=true
keyFile=key/keyFile.file

--启动节点
./bin/mongod -f shard/shard3/shard3-57011.conf
./bin/mongod -f shard/shard3/shard3-57013.conf
./bin/mongod -f shard/shard3/shard3-57015.conf
./bin/mongod -f shard/shard3/shard3-57017.conf

--配置节点集群
./bin/mongo --port 57011
var cfg = {"_id":"shard3", 
    "protocolVersion" : 1, 
    "members":[ 
        {"_id":1,"host":"192.168.238.153:57011"}, 
        {"_id":2,"host":"192.168.238.153:57013"}, 
        {"_id":3,"host":"192.168.238.153:57015"}, 
        {"_id":4,"host":"192.168.238.153:57017","arbiterOnly":true}
    ]
};
rs.initiate(cfg)
```

![image-20200706064637478](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200706064637478.png)

**4.shard4(58011,58013,58015,58017)**

```
--shard4-58011.conf
dbpath=shard/shard4/shard4-58011
bind_ip=0.0.0.0
port=58011
fork=true
logpath=shard/shard4/logs/shard4-58011.log
replSet=shard4
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard4-58013.conf
dbpath=shard/shard4/shard4-58013
bind_ip=0.0.0.0
port=58013
fork=true
logpath=shard/shard4/logs/shard4-58013.log
replSet=shard4
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard4-58015.conf
dbpath=shard/shard4/shard4-58015
bind_ip=0.0.0.0
port=58015
fork=true
logpath=shard/shard4/logs/shard4-58015.log
replSet=shard4
shardsvr=true
auth=true
keyFile=key/keyFile.file

--shard4-58017.conf
dbpath=shard/shard4/shard4-58017
bind_ip=0.0.0.0
port=58017
fork=true
logpath=shard/shard4/logs/shard4-58017.log
replSet=shard4
shardsvr=true
auth=true
keyFile=key/keyFile.file

--启动节点
./bin/mongod -f shard/shard4/shard4-58011.conf
./bin/mongod -f shard/shard4/shard4-58013.conf
./bin/mongod -f shard/shard4/shard4-58015.conf
./bin/mongod -f shard/shard4/shard4-58017.conf

--配置节点集群
./bin/mongo --port 58011
var cfg = {"_id":"shard4", 
    "protocolVersion" : 1, 
    "members":[ 
        {"_id":1,"host":"192.168.238.153:58011"}, 
        {"_id":2,"host":"192.168.238.153:58013"}, 
        {"_id":3,"host":"192.168.238.153:58015"}, 
        {"_id":4,"host":"192.168.238.153:58017","arbiterOnly":true}
    ]
};
rs.initiate(cfg)
```

![image-20200706064726115](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200706064726115.png)

**五、配置路由route节点**

```
--route-27017.conf 
port=27017
bind_ip=0.0.0.0
fork=true
logpath=route/logs/route.log
configdb=configsvr/192.168.238.153:17011,192.168.238.153:17013,192.168.238.153:17015
keyFile=key/keyFile.file

--启动节点
./bin/mongos -f route/route-27017.conf


./bin/mongo --port 27017

--创建root用户
use admin
db.createUser({
    user:"root",
    pwd:"abc321",
    roles:[{role:"root",db:"admin"}]
})
db.auth("root","abc321")
show dbs

--mongos（路由）中添加分片节点
sh.status()
sh.addShard("shard1/192.168.238.153:37011,192.168.238.153:37013,192.168.238.153:37015,192.168.238.153:37017");
sh.addShard("shard2/192.168.238.153:47011,192.168.238.153:47013,192.168.238.153:47015,192.168.238.153:47017");
sh.addShard("shard3/192.168.238.153:57011,192.168.238.153:57013,192.168.238.153:57015,192.168.238.153:57017");
sh.addShard("shard4/192.168.238.153:58011,192.168.238.153:58013,192.168.238.153:58015,192.168.238.153:58017");
sh.status()

--为数据库开启分片功能 
sh.enableSharding("lg_resume") 
--为指定集合开启分片功能 
sh.shardCollection("lg_resume.lagou_resume_datas",{"name":"hashed"})

--创建用户lagou_gx
use lg_resume
db.createUser({
    user:"lagou_gx",
    pwd:"abc321",
    roles:[{role:"readWrite",db:"lg_resume"}]
})
db.auth("lagou_gx","abc321")
show dbs
```

![image-20200706072211172](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200706072211172.png)

![image-20200706072745285](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200706072745285.png)

**六、springboot项目**

**1.实体类Resume**

```
public class Resume {

    private String id;
    private String name;
    private String city;
    private Date birthday;
    private double expectSalary;

}
```

**2.repository ResumeDao及实现类ResumeDaoImpl**

```
public interface ResumeDao {

    void insertResume(Resume resume);

    Resume findByName(String name);

    List<Resume> findListByName(String name);

    List<Resume> findListByNameAndExpectSalary(String name, double expectSalary);
}


@Repository
public class ResumeDaoImpl implements ResumeDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insertResume(Resume resume) {
//        mongoTemplate.insert(resume);
        mongoTemplate.insert(resume, "lg_resume_datas");
    }

    @Override
    public Resume findByName(String name) {
        List<Resume> datas = findListByName(name);
        return datas.isEmpty() ? null : datas.get(0);
    }

    @Override
    public List<Resume> findListByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.find(query, Resume.class, "lg_resume_datas");
    }

    @Override
    public List<Resume> findListByNameAndExpectSalary(String name, double expectSalary) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name).andOperator(Criteria.where("expectSalary").is(expectSalary)));
        return mongoTemplate.find(query, Resume.class, "lg_resume_datas");
    }
}

```

**3.测试类**

```
@SpringBootApplication
public class MongoRootMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MongoRootMain.class, args);
        ResumeDao resumeDao = applicationContext.getBean(ResumeDao.class);
        Resume resume = new Resume();

        resume.setName("lisi");
        resume.setCity("北京");
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2000-01-01 12:12:13");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        resume.setBirthday(date);
        resume.setExpectSalary(23000);
        resumeDao.insertResume(resume);
        System.out.println("resume---->" + resume);

        Resume lisi = resumeDao.findByName("lisi");
        System.out.println("lisi:" + lisi);

    }
}

```

**4.application.properties**

```
spring.data.mongodb.host=192.168.238.153
spring.data.mongodb.port=27017
spring.data.mongodb.database=lg_resume
spring.data.mongodb.username=lagou_gx
spring.data.mongodb.password=abc321
```

**5.测试**

![image-20200706075815986](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200706075815986.png)

