package com.example.springcloud.consumer.controller;

import com.example.springcloud.consumer.feign.FeignClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @description 消费者测试
 * @date 2019/11/3/0003
 */

@RestController
@RequestMapping("/consumer")
public class TestController {

    @Autowired
    private FeignClients feignClients;

    @RequestMapping("/get")
    public String test(){
        return feignClients.get();
    }
}
