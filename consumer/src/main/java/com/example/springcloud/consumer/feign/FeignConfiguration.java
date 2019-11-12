package com.example.springcloud.consumer.feign;

import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LDL
 * @Date: 2019/10/27/0027 21:33
 * @Description:
 */
@Configuration
public class FeignConfiguration {
    /**
     * 日志级别
     * NONE：不输出日志。
     * BASIC：只输出请求方法的 URL 和响应的状态码以及接口执行的时间。
     * HEADERS：将 BASIC 信息和请求头信息输出。
     * FULL：输出完整的请求信息。
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "password");
    }
}
