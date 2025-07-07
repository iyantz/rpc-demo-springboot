package com.rpc.demo.core.client;

import com.rpc.demo.common.RpcRequest;
import com.rpc.demo.common.RpcResponse;
import com.rpc.demo.core.protocol.RpcDecoder;
import com.rpc.demo.core.protocol.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * RPC客户端
 */
@Slf4j
public class RpcClient {
    
    private final String host;
    private final int port;
    private Channel channel;
    private EventLoopGroup group;
    private final ConcurrentMap<String, CompletableFuture<RpcResponse>> pendingRequests = new ConcurrentHashMap<>();
    
    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void connect() throws InterruptedException {
        group = new NioEventLoopGroup();
        
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        
                        // 添加编解码器
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
                        pipeline.addLast(new LengthFieldPrepender(4));
                        pipeline.addLast(new RpcEncoder());
                        pipeline.addLast(new RpcDecoder(RpcResponse.class));
                        
                        // 添加业务处理器
                        pipeline.addLast(new RpcClientHandler(pendingRequests));
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        
        ChannelFuture future = bootstrap.connect(host, port).sync();
        channel = future.channel();
        
        log.info("RPC客户端连接成功: {}:{}", host, port);
    }
    
    public CompletableFuture<RpcResponse> send(RpcRequest request) {
        CompletableFuture<RpcResponse> future = new CompletableFuture<>();
        pendingRequests.put(request.getRequestId(), future);
        
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()) {
                    future.completeExceptionally(channelFuture.cause());
                    pendingRequests.remove(request.getRequestId());
                }
            }
        });
        
        return future;
    }
    
    public void close() {
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
        log.info("RPC客户端关闭");
    }
    
} 