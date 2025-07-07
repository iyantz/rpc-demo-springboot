package com.rpc.demo.client.controller;

import com.rpc.demo.common.dto.User;
import com.rpc.demo.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        log.info("REST请求: 根据ID获取用户 {}", id);
        return userService.getUserById(id);
    }
    
    /**
     * 获取所有用户
     */
    @GetMapping
    public List<User> getAllUsers() {
        log.info("REST请求: 获取所有用户");
        return userService.getAllUsers();
    }
    
    /**
     * 创建用户
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("REST请求: 创建用户 {}", user.getUsername());
        return userService.createUser(user);
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        log.info("REST请求: 更新用户 {}", id);
        user.setId(id);
        return userService.updateUser(user);
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable("id") Long id) {
        log.info("REST请求: 删除用户 {}", id);
        return userService.deleteUser(id);
    }
    
    /**
     * 根据用户名获取用户
     */
    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable("username") String username) {
        log.info("REST请求: 根据用户名获取用户 {}", username);
        return userService.getUserByUsername(username);
    }
    
} 