package com.rpc.demo.core.proxy;

import com.rpc.demo.common.RpcRequest;
import com.rpc.demo.common.RpcResponse;
import com.rpc.demo.core.client.RpcClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * RPC代理
 */
@Slf4j
public class RpcProxy implements InvocationHandler {
    
    private final RpcClient rpcClient;
    private final String serviceName;
    private final String version;
    
    public RpcProxy(RpcClient rpcClient, String serviceName, String version) {
        this.rpcClient = rpcClient;
        this.serviceName = serviceName;
        this.version = version;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构建RPC请求
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setServiceName(serviceName);
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        request.setVersion(version);
        
        log.info("发送RPC请求: {}", request.getRequestId());
        
        // 发送请求
        CompletableFuture<RpcResponse> future = rpcClient.send(request);
        RpcResponse response = future.get();
        
        if (response.isSuccess()) {
            return response.getResult();
        } else {
            throw new RuntimeException("RPC调用失败: " + response.getErrorMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T create(RpcClient rpcClient, Class<T> interfaceClass, String version) {
        return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[]{interfaceClass},
            new RpcProxy(rpcClient, interfaceClass.getName(), version)
        );
    }
    
} 