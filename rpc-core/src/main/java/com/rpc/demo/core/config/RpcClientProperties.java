package com.rpc.demo.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RPC客户端配置属性
 */
@Data
@ConfigurationProperties(prefix = "rpc.client")
public class RpcClientProperties {
    
    /**
     * 服务器地址
     */
    private String host = "localhost";
    
    /**
     * 服务器端口
     */
    private int port = 8888;
    
} 