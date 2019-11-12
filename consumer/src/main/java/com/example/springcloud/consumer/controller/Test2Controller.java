package com.example.springcloud.consumer.controller;

import com.example.springcloud.consumer.feign.FeignClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LDL
 * @description
 * @date 2019/11/10/0010 17:27
 */

@RestController
@RequestMapping("/feign/consumer")
public class Test2Controller {

    @Autowired
    private FeignClients feignClients;

    @RequestMapping("/get")
    public String test(){
        return feignClients.get();
    }
}
