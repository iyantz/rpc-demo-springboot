package com.rpc.demo.server.config;

import com.rpc.demo.common.annotation.RpcService;
import com.rpc.demo.core.server.RpcServer;
import com.rpc.demo.core.server.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * RPC服务器配置
 */
@Slf4j
@Configuration
public class RpcServerConfig implements ApplicationContextAware, CommandLineRunner {
    
    private ApplicationContext applicationContext;
    private RpcServer rpcServer;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Bean
    public ServiceRegistry serviceRegistry() {
        return new ServiceRegistry();
    }
    
    @Bean
    public BeanPostProcessor rpcServiceProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                Class<?> beanClass = bean.getClass();
                if (beanClass.isAnnotationPresent(RpcService.class)) {
                    RpcService rpcService = beanClass.getAnnotation(RpcService.class);
                    Class<?> interfaceClass = rpcService.interfaceClass();
                    
                    // 如果没有指定接口类，使用默认的接口
                    if (interfaceClass == void.class) {
                        Class<?>[] interfaces = beanClass.getInterfaces();
                        if (interfaces.length > 0) {
                            interfaceClass = interfaces[0];
                        }
                    }
                    
                    if (interfaceClass != void.class) {
                        String serviceName = interfaceClass.getName();
                        serviceRegistry().register(serviceName, bean);
                        log.info("注册RPC服务: {} -> {}", serviceName, bean.getClass().getName());
                    }
                }
                return bean;
            }
        };
    }
    
    @Override
    public void run(String... args) throws Exception {
        // 启动RPC服务器
        int port = 9999;
        rpcServer = new RpcServer(port, serviceRegistry());
        
        // 在新线程中启动服务器
        Thread serverThread = new Thread(() -> {
            try {
                rpcServer.start();
            } catch (InterruptedException e) {
                log.error("RPC服务器启动失败", e);
            }
        });
        serverThread.start();
        
        log.info("RPC服务器启动线程已启动，端口: {}", port);
    }
    
} 