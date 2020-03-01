一、简单题
1、Mybatis动态sql是做什么的？都有哪些动态sql？简述一下动态sql的执行原理？

（1）mybatis动态sql是mybatis的强大特性之一， 用if、foreach等标签更优雅更方便的帮助我们拼接sql语句

（2）动态sql标签包括 if、choose(when, otherwise) 、trim (where, set)、foreach

（3）动态sql执行的原理

​         参考mybatis动态sql执行原理 .docx

2、Mybatis是否支持延迟加载？如果支持，它的实现原理是什么？

（1）支持延迟加载，支持association关联对象和collection关联集合对象的延迟懒加载

（2）实现原理

使用CGLIB创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，在进行结果映射时，将事先保存好的对象查询关联sql进行查询，调用set方法赋值

3、Mybatis都有哪些Executor执行器？它们之间的区别是什么？

（1）有BaseExecutor、BatchExecutor、CachingExecutor、ClosedExecutor、ReuseExecutor、SimpleExecutor

（2）区别

BaseExecutor是抽象类，是SimpleExecutor、BatchExecutor、ReuseExecutor的父类，实现了执行器的基本功能

SimpleExecutor最简单的执行器，根据对应的sql直接执行

BatchExecutor 批处理的执行器

ReuseExecutor可重用的执行器，重用的对象是Statement，用HashMap存储Statement

CachingExecutor是二级缓存的执行器，通过代理的 Executor 执行基本功能

4、简述下Mybatis的一级、二级缓存（分别从存储结构、范围、失效场景。三个方面来作答）？

（1）一级缓存  

​	一级缓存是sqlSession级别的，存储结构是HashMap  key是CacheKey对象，value是查询的结果数据

当执行增删改、close、commit等方法时，会清空一级缓存导致缓存失效

（2）二级缓存

二级缓存是mapper级别的，多个sqlSession可以共享同一个mapper的二级缓存

底层存储结构也是HashMap key是CacheKey对象，value是查询的结果数据,通过TransactionalCacheManager管理

执行增删改、close、commit等方法时，会清空二级缓存

5、简述Mybatis的插件运行原理，以及如何编写一个插件？

（1）运行原理

Mybatis可以编写ParameterHandler、ResultSetHandler、StatementHandler、Executor这4种接口的插件，在创建这四个接口的对象时，多个插件（实现Interceptor接口的拦截器）组成拦截器链，通过interceptorChain.pluginAll，遍历所有的插件，生成代理对象,执行拦截的方法时，会增强

（2）如何编写一个插件

1）实现Interceptor接口，复写intercept()方法，加@Signature注解，指定拦截的接口、方法和参数

2）在sqlMapConfig.xml核心配置文件中，将插件加在<plugins></plugins>标签里



二、编程题
请完善自定义持久层框架IPersistence，在现有代码基础上添加、修改及删除功能。【需要采用getMapper方式】

github地址：https://github.com/limnxmj/lagou/tree/master/mybatis