1.zhangsan拥有DemoController中所有方法的所有权限
http://localhost:8080/demo/query?username=zhangsan
http://localhost:8080/demo/handle01?username=zhangsan
http://localhost:8080/demo/handle02?username=zhangsan
http://localhost:8080/demo/handle03?username=zhangsan

2.lisi拥有DemoController中handle01和handle02权限
http://localhost:8080/demo/handle01?username=lisi
http://localhost:8080/demo/handle02?username=lisi

lisi没有DemoController中query和handle03权限
http://localhost:8080/demo/query?username=lisi
http://localhost:8080/demo/handle03?username=lisi

3.wangwu拥有DemoController中handle01和handle03权限
http://localhost:8080/demo/handle01?username=wangwu
http://localhost:8080/demo/handle03?username=wangwu

wangwu没有DemoController中query和handle02权限
http://localhost:8080/demo/query?username=wangwu
http://localhost:8080/demo/handle02?username=wangwu