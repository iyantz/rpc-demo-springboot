package com.rpc.demo.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * RPC服务注解
 * 用于标识RPC服务提供者
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {
    
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
     * 服务权重
     */
    int weight() default 1;
    
} 