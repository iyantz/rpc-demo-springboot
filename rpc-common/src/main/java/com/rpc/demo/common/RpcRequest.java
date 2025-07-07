package com.rpc.demo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * RPC请求实体类
 */
@Data
public class RpcRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 请求ID
     */
    private String requestId;
    
    /**
     * 服务名称
     */
    private String serviceName;
    
    /**
     * 方法名称
     */
    private String methodName;
    
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    
    /**
     * 参数值
     */
    private Object[] parameters;
    
    /**
     * 版本号
     */
    private String version;
    
    /**
     * 超时时间
     */
    private long timeout;
    
} 