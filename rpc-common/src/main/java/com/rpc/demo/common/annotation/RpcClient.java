package com.rpc.demo.common.annotation;

import java.lang.annotation.*;

/**
 * RPC客户端注解
 * 用于标识RPC服务消费者
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcClient {
    
    /**
     * 服务接口类
     */
    Class<?> interfaceClass() default void.class;
    
    /**
     * 服务版本号
     */
    String version() default "1.0";
    
    /**
     * 服务分组
     */
    String group() default "";
    
    /**
     * 超时时间（毫秒）
     */
    long timeout() default 5000;
    
    /**
     * 服务器地址
     */
    String serverAddress() default "localhost:8080";
    
} 