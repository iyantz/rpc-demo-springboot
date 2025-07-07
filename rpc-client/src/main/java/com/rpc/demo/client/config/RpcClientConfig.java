package com.rpc.demo.client.config;

import com.rpc.demo.common.service.UserService;
import com.rpc.demo.core.client.RpcClient;
import com.rpc.demo.core.proxy.RpcProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RPC客户端配置
 */
@Slf4j
@Configuration
public class RpcClientConfig {
    
    @Value("${rpc.server.host:localhost}")
    private String serverHost;
    
    @Value("${rpc.server.port:9999}")
    private int serverPort;
    
    @Bean
    public RpcClient rpcClient() throws InterruptedException {
        RpcClient client = new RpcClient(serverHost, serverPort);
        client.connect();
        log.info("RPC客户端连接成功: {}:{}", serverHost, serverPort);
        return client;
    }
    
    @Bean
    public UserService userService(RpcClient rpcClient) {
        return RpcProxy.create(rpcClient, UserService.class, "1.0");
    }
    
} 