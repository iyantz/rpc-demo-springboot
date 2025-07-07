package com.rpc.demo.core.protocol;

import com.rpc.demo.core.serialization.JsonSerializer;
import com.rpc.demo.core.serialization.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * RPC解码器
 */
public class RpcDecoder extends ByteToMessageDecoder {
    
    private final Class<?> genericClass;
    private final Serializer serializer;
    
    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
        this.serializer = new JsonSerializer();
    }
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 数据包长度不够，跳过
        if (in.readableBytes() < 4) {
            return;
        }
        
        // 标记读取位置
        in.markReaderIndex();
        
        // 读取数据长度
        int dataLength = in.readInt();
        
        // 数据包长度不够，恢复读取位置
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        
        // 读取数据
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        
        // 反序列化
        Object object = serializer.deserialize(data, genericClass);
        out.add(object);
    }
    
} 