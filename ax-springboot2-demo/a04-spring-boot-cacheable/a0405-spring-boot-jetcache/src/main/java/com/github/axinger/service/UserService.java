package com.github.axinger.service;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.github.axinger.model.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Cached(name = "userCache-", key = "#userId", localExpire = 30, timeUnit = TimeUnit.MINUTES, expire = 30, cacheType = CacheType.REMOTE)
    public User getUserById(long userId) {
        System.out.println("userId = " + userId);
        return new User(userId, "userName@" + userId);
    }

    @CacheUpdate(name = "userCache-", key = "#user.userId", value = "#user")
    public void updateUser(User user) {
        System.out.println("update user:" + user);
    }

    //用于移除缓存元素
    @CacheInvalidate(name = "userCache-", key = "#userId")
    public void deleteUser(long userId) {
        System.out.println("delete user:" + userId);
    }
}
