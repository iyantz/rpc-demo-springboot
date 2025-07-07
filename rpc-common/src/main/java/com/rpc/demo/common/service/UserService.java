package com.rpc.demo.common.service;

import com.rpc.demo.common.dto.User;

import java.util.List;

/**
 * 用户服务接口
 * 用于演示RPC功能
 */
public interface UserService {
    
    /**
     * 根据ID获取用户
     */
    User getUserById(Long id);
    
    /**
     * 获取所有用户
     */
    List<User> getAllUsers();
    
    /**
     * 创建用户
     */
    User createUser(User user);
    
    /**
     * 更新用户
     */
    User updateUser(User user);
    
    /**
     * 删除用户
     */
    boolean deleteUser(Long id);
    
    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);
    
} 