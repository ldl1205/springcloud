package com.example.springcloud.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LDL
 * @description
 * @date 2019/11/10/0010 17:31
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/sayHello")
    public String sayHello() {
        return "";
    }

    @RequestMapping(value = "/fallback", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> fallback() {
        return new ResponseEntity<>("error.", HttpStatus.OK);
    }

    @RequestMapping(value = "/get/{time}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> get(@PathVariable("time") long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(port + " get ok.", HttpStatus.OK);
    }
}
