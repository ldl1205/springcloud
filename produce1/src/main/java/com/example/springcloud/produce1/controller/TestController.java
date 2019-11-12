package com.example.springcloud.produce1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @description 测试
 * @date 2019/11/3/0003
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/get")
    public String get(){
        return "i am from "+ port;
    }
}
