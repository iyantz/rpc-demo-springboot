package com.rpc.demo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RPC服务器启动类
 */
@SpringBootApplication
public class RpcServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RpcServerApplication.class, args);
    }
    
} 