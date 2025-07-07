package com.rpc.demo.server.service.impl;

import com.rpc.demo.common.annotation.RpcService;
import com.rpc.demo.common.dto.User;
import com.rpc.demo.common.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户服务实现类
 */
@Slf4j
@RpcService(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    
    private final ConcurrentMap<Long, User> userMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public UserServiceImpl() {
        // 初始化一些测试数据
        initTestData();
    }
    
    private void initTestData() {
        User user1 = new User(1L, "张三", "zhangsan@example.com");
        user1.setPhone("13888888888");
        user1.setAge(25);
        user1.setGender("男");
        
        User user2 = new User(2L, "李四", "lisi@example.com");
        user2.setPhone("13999999999");
        user2.setAge(30);
        user2.setGender("女");
        
        User user3 = new User(3L, "王五", "wangwu@example.com");
        user3.setPhone("13777777777");
        user3.setAge(28);
        user3.setGender("男");
        
        userMap.put(1L, user1);
        userMap.put(2L, user2);
        userMap.put(3L, user3);
        
        idGenerator.set(4);
    }
    
    @Override
    public User getUserById(Long id) {
        log.info("根据ID获取用户: {}", id);
        return userMap.get(id);
    }
    
    @Override
    public List<User> getAllUsers() {
        log.info("获取所有用户");
        return new ArrayList<>(userMap.values());
    }
    
    @Override
    public User createUser(User user) {
        log.info("创建用户: {}", user.getUsername());
        Long id = idGenerator.getAndIncrement();
        user.setId(id);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMap.put(id, user);
        return user;
    }
    
    @Override
    public User updateUser(User user) {
        log.info("更新用户: {}", user.getId());
        User existingUser = userMap.get(user.getId());
        if (existingUser != null) {
            user.setUpdateTime(LocalDateTime.now());
            userMap.put(user.getId(), user);
            return user;
        }
        return null;
    }
    
    @Override
    public boolean deleteUser(Long id) {
        log.info("删除用户: {}", id);
        User removed = userMap.remove(id);
        return removed != null;
    }
    
    @Override
    public User getUserByUsername(String username) {
        log.info("根据用户名获取用户: {}", username);
        return userMap.values().stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }
    
} 