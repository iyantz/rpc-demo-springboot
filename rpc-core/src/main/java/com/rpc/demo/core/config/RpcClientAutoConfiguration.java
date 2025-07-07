package com.rpc.demo.core.config;

import com.rpc.demo.common.annotation.EnableRpcClients;
import com.rpc.demo.core.client.RpcClient;
import com.rpc.demo.core.proxy.RpcProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

/**
 * RPC客户端自动配置
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RpcClientProperties.class)
public class RpcClientAutoConfiguration implements ApplicationContextAware {
    
    private ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Bean
    public RpcClient rpcClient(RpcClientProperties properties) throws InterruptedException {
        RpcClient client = new RpcClient(properties.getHost(), properties.getPort());
        client.connect();
        log.info("RPC客户端连接成功: {}:{}", properties.getHost(), properties.getPort());
        return client;
    }
    
    /**
     * 自动扫描@RpcClient注解并注入代理对象
     */
    @Bean
    public BeanPostProcessor rpcClientProcessor(RpcClient rpcClient) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                Class<?> beanClass = bean.getClass();
                
                // 检查是否启用了RPC客户端扫描
                if (shouldProcessBean(beanClass)) {
                    processRpcClientFields(bean, beanClass, rpcClient);
                }
                
                return bean;
            }
        };
    }
    
    private boolean shouldProcessBean(Class<?> beanClass) {
        // 检查应用是否标注了@EnableRpcClients
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(EnableRpcClients.class);
        return beanNames.length > 0;
    }
    
    private void processRpcClientFields(Object bean, Class<?> beanClass, RpcClient rpcClient) {
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
                        rpcClient, 
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
    }
    
}