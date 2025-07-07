package com.rpc.demo.core.protocol;

import com.rpc.demo.core.serialization.JsonSerializer;
import com.rpc.demo.core.serialization.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RPC编码器
 */
public class RpcEncoder extends MessageToByteEncoder<Object> {
    
    private final Serializer serializer;
    
    public RpcEncoder() {
        this.serializer = new JsonSerializer();
    }
    
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 序列化数据
        byte[] data = serializer.serialize(msg);
        
        // 写入数据长度
        out.writeInt(data.length);
        
        // 写入数据
        out.writeBytes(data);
    }
    
} 