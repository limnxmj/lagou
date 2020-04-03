tomcat主要分为Connector和Container
connector负责对外交流： 处理Socket连接，负责⽹络字节流与Request和Response对象的转化
Container负责内部处理：加载和管理Servlet，以及具体处理Request请求

其中connector包括EndPoint、Processor、ProtocolHandler、Adapter
EndPoint:是 Coyote 通信端点，即通信监听的接⼝，是具体Socket接收和发送处理器，是对传输层的抽象，因此EndPoint⽤来实现TCP/IP协议的
Processor:是Coyote 协议处理接⼝,如果说EndPoint是⽤来实现TCP/IP协议的，那么Processor⽤来实现HTTP协议，Processor接收来⾃EndPoint的Socket，读取字节流解析成Tomcat Request和Response对象，并通过Adapter将其提交到容器处理，Processor是对应⽤层协议的抽象
ProtocolHandler:是Coyote 协议接⼝， 通过Endpoint 和 Processor, 实现针对具体协议的处理能⼒。Tomcat 按照协议和I/O 提供了6个实现类：AjpNioProtocol, AjpAprProtocol, AjpNio2Protocol, Http11NioProtocol,Http11Nio2Protocol,Http11AprProtocol
Adapter:由于协议不同, 客户端发过来的请求信息也不尽相同, Tomcat定义了⾃⼰的Request类来封装这些请求信息。ProtocolHandler接⼝负责解析请求并⽣成Tomcat Request类。但是这个Request对象不是标准的ServletRequest, 不能⽤Tomcat Request作为参数来调⽤容器。Tomcat设计者的解决⽅案是引⼊CoyoteAdapter, 这是适配器模式的经典运⽤, 连接器调⽤CoyoteAdapter的Sevice⽅法, 传⼊的是Tomcat Request对象, CoyoteAdapter负责将Tomcat Request转成ServletRequest, 再调⽤容器

Container组件下有⼏种具体的组件，分别是Engine、Host、Context和Wrapper。
Engine:表示整个Catalina的Servlet引擎，⽤来管理多个虚拟站点，⼀个Service最多只能有⼀个Engine，但是⼀个引擎可包含多个Host
Host:代表⼀个虚拟主机，或者说⼀个站点，可以给Tomcat配置多个虚拟主机地址，⽽⼀个虚拟主机下可包含多个Context
Context:表示⼀个Web应⽤程序， ⼀个Web应⽤可包含多个Wrapper
Wrapper:表示⼀个Servlet，Wrapper 作为容器中的最底层，不能包含⼦容器