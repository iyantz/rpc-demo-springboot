package com.rpc.demo.core.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * JSON序列化实现
 */
@Slf4j
public class JsonSerializer implements Serializer {
    
    private final ObjectMapper objectMapper;
    
    public JsonSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    @Override
    public <T> byte[] serialize(T object) {
        try {
            String jsonString = objectMapper.writeValueAsString(object);
            return jsonString.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("序列化失败", e);
            throw new RuntimeException("序列化失败", e);
        }
    }
    
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            String jsonString = new String(bytes, StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            log.error("反序列化失败", e);
            throw new RuntimeException("反序列化失败", e);
        }
    }
    
} 