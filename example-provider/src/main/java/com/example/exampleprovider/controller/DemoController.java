package com.example.exampleprovider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class DemoController {

    /*
      因为在yml中配置了service-url.nacos-user-service，
      这里不需要再定义要访问微服务名常量，而是通过boot直接读出来
       */
    @Value("${service-url.nacos-user-service}")
    private String serverURL;

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/sayhi")
    public String sayhi() {
        return restTemplate.getForObject(serverURL + "/sayhi/", String.class);
    }
}



