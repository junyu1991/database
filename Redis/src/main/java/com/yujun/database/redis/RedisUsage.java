package com.yujun.database.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author hunter
 * @version 1.0.0
 * @date 10/16/19 10:32 PM
 * @description TODO
 **/
public class RedisUsage {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    public void addLink(String userID, String url) {
        this.listOperations.leftPush(userID, url);
    }

    public void redisTemplate() {

    }
}
