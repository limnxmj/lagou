**solr自带中文分词器**

**1.复制jar包**

~~~
cp /root/solr/solr-7.7.3/contrib/analysis-extras/lucene-libs/lucene-analyzers-smartcn-7.7.3.jar /root/solr/apache-tomcat-8.5.58/webapps/solr/WEB-INF/lib/
~~~

**2.修改配置文件managed-schema**

~~~
cd /usr/local/solr/solrhome/new_core/conf
vim managed-schema

<fieldType name="text_hmm_chinese" class="solr.TextField" positionIncrementGap="100"> 
    <analyzer type="index"> 
        <tokenizer class="org.apache.lucene.analysis.cn.smart.HMMChineseTokenizerFactory"/> 
    </analyzer> 
    <analyzer type="query"> 
        <tokenizer class="org.apache.lucene.analysis.cn.smart.HMMChineseTokenizerFactory"/> 
    </analyzer>
</fieldType>
~~~

**3.重启tomcat**

~~~
cd /root/solr/apache-tomcat-8.5.58
./bin/shutdown.sh
./bin/startup.sh
~~~

![image-20200918155940208](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200918155940208.png)



**IK中文分词器**

**1.下载分词器**

~~~
wget https://search.maven.org/remotecontent?filepath=com/github/magese/ik-analyzer-solr7/7.x/ik-analyzer-solr7-7.x.jar
~~~

**2.复制jar包**

~~~
cp ik-analyzer-solr7-7.x.jar /root/solr/apache-tomcat-8.5.58/webapps/solr/WEB-INF/lib/
~~~

**3.修改配置文件managed-schema**

~~~
cd /usr/local/solr/solrhome/new_core/conf
vim managed-schema

<!-- ik分词器 --> 
<fieldType name="text_ik" class="solr.TextField">
    <analyzer type="index">
        <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="false" conf="ik.conf"/>
        <filter class="solr.LowerCaseFilterFactory"/>
    </analyzer>
    <analyzer type="query">
        <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="true" conf="ik.conf"/>
        <filter class="solr.LowerCaseFilterFactory"/>
    </analyzer>
</fieldType>
~~~

**4.重启tomcat**

~~~
cd /root/solr/apache-tomcat-8.5.58
./bin/shutdown.sh
./bin/startup.sh
~~~

![image-20200918161240643](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200918161240643.png)

**5.停用词**

~~~
vim managed-schema
<filter class="solr.StopFilterFactory" words="stopwords.txt"/>
~~~

~~~
vim stopwords.txt
中华
~~~

![image-20200918161417138](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200918161417138.png)

![image-20200918162023813](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200918162023813.png)





**使用dataimport 导入数据库数据**

~~~
cd /usr/local/solr/solrhome/new_core/conf
vim data-config.xml

<dataConfig> 
    <!-- 这是mysql的配置 --> 
    <dataSource driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost:3306/solr_book" user="root" password="root"/> 
    <document> 
        <!-- query是一条sql，代表在数据库查找出来的数据 --> 
        <entity name="book" query="select * from book"> 
            <!-- 每一个field映射着数据库中列与文档中的域，column是数据库列，name是 solr的域(必须是在managed-schema文件中配置过的域才行) -->
            <field column="id" name="id"/>
            <field column="name" name="name"/>
            <field column="price" name="price"/>
            <field column="description" name="desc"/>
        </entity>
    </document>
</dataConfig>
~~~

~~~
vim solrconfig.xml
<requestHandler name="/dataimport" class="solr.DataImportHandler">
    <lst name="defaults">
        <str name="config">data-config.xml</str>
    </lst>
</requestHandler>
~~~

~~~
vim managed-schema
<field name="name" type="text_ik" indexed="true" stored="true"/>
<field name="price" type="pfloat" indexed="true" stored="true"/>
<field name="desc" type="text_ik" indexed="true" stored="false"/>
~~~

![image-20200918175443964](C:\Users\MingLi\AppData\Roaming\Typora\typora-user-images\image-20200918175443964.png)