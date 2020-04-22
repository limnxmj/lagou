package cn.xmj.server;

import cn.xmj.handler.ServerHandler;
import com.lagou.utils.JSONSerializer;
import com.lagou.utils.RpcDecoder;
import com.lagou.utils.RpcRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

public class MyServerBootstrap implements ApplicationContextAware, InitializingBean {

    private String host;
    private int port;

    public static final Map<String, Object> beanMap = new HashMap<>(10);

    public MyServerBootstrap(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {

        new MyServerBootstrap("127.0.0.1", 8990).startServer();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(Service.class);
        if (serviceBeanMap != null && !serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                beanMap.put(serviceBean.getClass().getName(), serviceBean);
                Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
                for (Class clazz : interfaces) {
                    beanMap.put(clazz.getName(), serviceBean);
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startServer();
    }

    //host:ip地址  port:端口号
    private void startServer() throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new RpcDecoder(RpcRequest.class, new JSONSerializer()));
                        pipeline.addLast(new ServerHandler(beanMap));
                    }
                });
        serverBootstrap.bind(host, port).sync();
    }

}
