package com.rpc.demo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * RPC响应实体类
 */
@Data
public class RpcResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 请求ID
     */
    private String requestId;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 返回结果
     */
    private Object result;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 异常类型
     */
    private Class<? extends Throwable> exceptionType;
    
    /**
     * 创建成功响应
     */
    public static RpcResponse success(String requestId, Object result) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(requestId);
        response.setSuccess(true);
        response.setResult(result);
        return response;
    }
    
    /**
     * 创建失败响应
     */
    public static RpcResponse failure(String requestId, String errorMessage) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(requestId);
        response.setSuccess(false);
        response.setErrorMessage(errorMessage);
        return response;
    }
    
    /**
     * 创建异常响应
     */
    public static RpcResponse exception(String requestId, Throwable throwable) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(requestId);
        response.setSuccess(false);
        response.setErrorMessage(throwable.getMessage());
        response.setExceptionType(throwable.getClass());
        return response;
    }
    
} 