package com.rpc.demo.core.server;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 服务注册器
 */
@Slf4j
public class ServiceRegistry {
    
    private final ConcurrentMap<String, Object> serviceMap = new ConcurrentHashMap<>();
    
    /**
     * 注册服务
     */
    public void register(String serviceName, Object serviceBean) {
        serviceMap.put(serviceName, serviceBean);
        log.info("注册服务: {} -> {}", serviceName, serviceBean.getClass().getName());
    }
    
    /**
     * 获取服务
     */
    public Object getService(String serviceName) {
        return serviceMap.get(serviceName);
    }
    
    /**
     * 移除服务
     */
    public void unregister(String serviceName) {
        serviceMap.remove(serviceName);
        log.info("移除服务: {}", serviceName);
    }
    
    /**
     * 获取所有服务
     */
    public ConcurrentMap<String, Object> getAllServices() {
        return serviceMap;
    }
    
} 