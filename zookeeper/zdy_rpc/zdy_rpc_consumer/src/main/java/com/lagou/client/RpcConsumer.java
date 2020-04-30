package com.lagou.client;

import com.lagou.dao.IpDao;
import com.lagou.utils.JSONSerializer;
import com.lagou.utils.RpcEncoder;
import com.lagou.utils.RpcRequest;
import com.lagou.zookeeper.ZkClientUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcConsumer {

    //创建线程池对象
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final Map<String, ClientHandler> clientHandlerMapBak = new HashMap<>();
    private static final Map<String, ClientHandler> clientHandlerMap = new HashMap<>();
    private static final Set<String> serverIps = new HashSet<>();

    private static ZkClientUtil zkClientUtil;

    //1.创建一个代理对象
    public Object createProxy(final Class<?> serviceClass) {
        //借助JDK动态代理生成代理对象
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                // 调用初始化netty客户端的方法
                if (zkClientUtil == null) {
                    initClient();
                }
                if (clientHandlerMap.isEmpty()) {
                    throw new RuntimeException("no server");
                }

                long begin = System.currentTimeMillis();

                String key = zkClientUtil.getServer();
                if ("".equals(key)) {
                    throw new RuntimeException("no server");
                }
                ClientHandler clientHandler = clientHandlerMap.get(key);
                if (clientHandler == null) {
                    throw new RuntimeException("no server");
                }
                // 设置参数
                clientHandler.setPara(buildRpcRequest(proxy, method, args));

                // 去服务端请求数据
                Object result = executor.submit(clientHandler).get();

                long end = System.currentTimeMillis();

                zkClientUtil.writeData(key, end - begin, end);
                return result;
            }
        });
    }

    private RpcRequest buildRpcRequest(Object proxy, Method method, Object[] args) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameters(args);
        return rpcRequest;
    }

    //2.初始化netty客户端
    private static void initClient() throws InterruptedException {

        zkClientUtil = new ZkClientUtil();
        List<IpDao> serverList = zkClientUtil.getServerList();
        if (!serverList.isEmpty()) {
            for (IpDao ipDao : serverList) {
                String host = ipDao.getIp();
                int port = ipDao.getPort();
                String key = host + ":" + port;
                clientHandlerMap.put(key, connect(host, port));
                serverIps.add(key);
            }
        }
        clientHandlerMapBak.clear();
        clientHandlerMapBak.putAll(clientHandlerMap);


        ZkClient zkClient = zkClientUtil.getZkClient();
        zkClient.subscribeChildChanges(zkClientUtil.getZkBase(), new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {

                clientHandlerMap.clear();
                serverIps.clear();
                if (currentChilds != null && !currentChilds.isEmpty()) {
                    for (String child : currentChilds) {
                        if (clientHandlerMapBak.containsKey(child)) {
                            clientHandlerMap.put(child, clientHandlerMapBak.get(child));
                            serverIps.add(child);
                        }
                        clientHandlerMap.put(child, connect(child.split(":")[0], Integer.parseInt(child.split(":")[1])));
                        serverIps.add(child);
                    }
                }
                for (Map.Entry<String, ClientHandler> entry : clientHandlerMapBak.entrySet()) {
                    if (!clientHandlerMap.containsKey(entry.getKey())) {
                        ClientHandler clientHandler = entry.getValue();
                        clientHandler.close();
                    }
                }

                clientHandlerMapBak.clear();
                clientHandlerMapBak.putAll(clientHandlerMap);

                System.out.println("parentPath==>" + parentPath + ", currentChilds==>" + currentChilds);
            }
        });
    }

    private static ClientHandler connect(String host, int port) throws InterruptedException {
        ClientHandler clientHandler = new ClientHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new RpcEncoder(RpcRequest.class, new JSONSerializer()));
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(clientHandler);
                    }
                });
        bootstrap.connect(host, port).sync();
        clientHandler.setGroup(group);
        return clientHandler;
    }

}
