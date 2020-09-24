**一、下载安装ik分词器**

**1.下载安装**

~~~
/usr/local/elasticsearch/bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.3.0/elasticsearch-analysis-ik-7.3.0.zip
~~~

**2.下载完成后会提示 Continue with installation?输入 y 即可完成安装**

**3.重启Elasticsearch 和Kibana**



**二、扩展词**

~~~
1）新增自定义词典ext_dict.dic
cd /usr/local/elasticsearch/config/analysis-ik
vim ext_dict.dic
#输入：
江大桥
~~~

~~~xml
2)将自定义的扩展词典文件添加到IKAnalyzer.cfg.xml配置中
vim IKAnalyzer.cfg.xml

<!--用户可以在这里配置自己的扩展字典 -->
<entry key="ext_dict">ext_dict.dic</entry>
~~~

~~~
3)重启elasticsearch
~~~

~~~json
POST _analyze
{
  "analyzer": "ik_max_word",
  "text": "南京市长江大桥"
}
~~~

~~~json
POST _analyze
{
  "analyzer": "ik_smart",
  "text": "南京市长江大桥"
}
~~~

**三、停用词**

~~~
1)新增自定义词典stop_dict.dic
vim stop_dict.dic
#输入：
市长
~~~

~~~xml
2)将自定义的扩展词典文件添加到IKAnalyzer.cfg.xml配置中
vim IKAnalyzer.cfg.xml

<!--用户可以在这里配置自己的扩展停止词字典-->
<entry key="ext_stopwords">stop_dict.dic</entry>
~~~

~~~
3)重启elasticsearch
~~~

