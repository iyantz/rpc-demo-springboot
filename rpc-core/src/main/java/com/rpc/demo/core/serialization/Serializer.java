package com.rpc.demo.core.serialization;

/**
 * 序列化接口
 */
public interface Serializer {
    
    /**
     * 序列化
     */
    <T> byte[] serialize(T object);
    
    /**
     * 反序列化
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
    
} 