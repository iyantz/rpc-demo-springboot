package com.rpc.demo.common.annotation;

import java.lang.annotation.*;

/**
 * 启用RPC客户端自动扫描
 * 用于在Spring Boot应用中启用RPC客户端自动配置
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableRpcClients {
    
    /**
     * 要扫描的基础包路径
     */
    String[] basePackages() default {};
    
    /**
     * 要扫描的基础包类
     */
    Class<?>[] basePackageClasses() default {};
    
} 