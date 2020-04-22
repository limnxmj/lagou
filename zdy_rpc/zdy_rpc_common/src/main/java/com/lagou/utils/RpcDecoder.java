/**
 * FileName: RpcEncoder.java
 * Author:   limn_xmj@163.com
 * Date:     2020/4/22 8:41
 * Description:
 */
package com.lagou.utils;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    public static final int HEAD_LENGTH = 4;
    private Class<?> clazz;
    private Serializer serializer;

    public RpcDecoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (dataLength < 0) {
            channelHandlerContext.close();
        }
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
        }

        byte[] bytes = new byte[dataLength];
        byteBuf.readBytes(bytes);
        list.add(serializer.deserialize(clazz, bytes));

    }
}
