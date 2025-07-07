package com.rpc.demo.core.server;

import com.rpc.demo.common.RpcRequest;
import com.rpc.demo.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * RPC服务器处理器
 */
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    
    private final ServiceRegistry serviceRegistry;
    
    public RpcServerHandler(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        log.info("收到RPC请求: {}", request);
        
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        
        try {
            // 获取服务实例
            Object serviceBean = serviceRegistry.getService(request.getServiceName());
            if (serviceBean == null) {
                response.setSuccess(false);
                response.setErrorMessage("服务不存在: " + request.getServiceName());
                ctx.writeAndFlush(response);
                return;
            }
            
            // 获取方法
            Method method = serviceBean.getClass().getMethod(
                request.getMethodName(), 
                request.getParameterTypes()
            );
            
            // 执行方法
            Object result = method.invoke(serviceBean, request.getParameters());
            
            // 设置响应
            response.setSuccess(true);
            response.setResult(result);
            
            log.info("RPC请求处理成功: {}", request.getRequestId());
            
        } catch (Exception e) {
            log.error("RPC请求处理失败", e);
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
            response.setExceptionType(e.getClass());
        }
        
        ctx.writeAndFlush(response);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("RPC服务器异常", cause);
        ctx.close();
    }
    
} 