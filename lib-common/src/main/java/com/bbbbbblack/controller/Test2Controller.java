package com.bbbbbblack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test2Controller {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @RequestMapping("/test2")
    public void test() {
        redisTemplate.convertAndSend("returnBook", "123455");
    }
}
