package com.rpc.demo.client.config;

import com.rpc.demo.core.client.RpcClient;
import com.rpc.demo.core.proxy.RpcProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

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
    
    /**
     * 自动扫描@RpcClient注解并注入代理对象
     */
    @Bean
    public BeanPostProcessor rpcClientProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                Class<?> beanClass = bean.getClass();
                Field[] fields = beanClass.getDeclaredFields();
                
                for (Field field : fields) {
                    if (field.isAnnotationPresent(com.rpc.demo.common.annotation.RpcClient.class)) {
                        com.rpc.demo.common.annotation.RpcClient rpcClientAnnotation = field.getAnnotation(com.rpc.demo.common.annotation.RpcClient.class);
                        
                        // 获取接口类型
                        Class<?> interfaceClass = rpcClientAnnotation.interfaceClass();
                        if (interfaceClass == void.class) {
                            interfaceClass = field.getType();
                        }
                        
                        try {
                            // 创建代理对象
                            Object proxy = RpcProxy.create(
                                rpcClient(), 
                                interfaceClass, 
                                rpcClientAnnotation.version()
                            );
                            
                            // 注入代理对象
                            field.setAccessible(true);
                            field.set(bean, proxy);
                            
                            log.info("自动注入RPC客户端: {} -> {}", 
                                field.getName(), interfaceClass.getName());
                            
                        } catch (Exception e) {
                            log.error("注入RPC客户端失败: {}", field.getName(), e);
                            throw new RuntimeException("注入RPC客户端失败", e);
                        }
                    }
                }
                
                return bean;
            }
        };
    }
    
} 