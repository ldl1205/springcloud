package com.example.springcloud.produce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProduceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProduceApplication.class, args);
    }

}
