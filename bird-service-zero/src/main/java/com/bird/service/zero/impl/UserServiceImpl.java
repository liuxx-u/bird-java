package com.bird.service.zero.impl;

import com.bird.core.service.AbstractServiceImpl;
import com.bird.service.zero.UserService;
import com.bird.service.zero.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * Created by liuxx on 2017/10/11.
 */
@Service
@CacheConfig(cacheNames = "zero_user")
@com.alibaba.dubbo.config.annotation.Service
public class UserServiceImpl extends AbstractServiceImpl<User> implements UserService {
}
