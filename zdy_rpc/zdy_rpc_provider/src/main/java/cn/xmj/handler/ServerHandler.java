package cn.xmj.handler;

import com.lagou.utils.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Object> beanMap;

    public ServerHandler(Map<String, Object> beanMap) {
        this.beanMap = beanMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        RpcRequest rpcRequest = (RpcRequest) msg;

        // 判断是否符合约定，符合则调用本地方法，返回数据
        String className = rpcRequest.getClassName();
        Object bean = beanMap.get(className);
        if (bean != null) {
//            UserService bean = (UserService) Class.forName(className).newInstance();
            Method method = bean.getClass().getDeclaredMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            method.invoke(bean, rpcRequest.getParameters());
            ctx.writeAndFlush("success");
            return;
        }
        ctx.writeAndFlush("error");
    }

}
