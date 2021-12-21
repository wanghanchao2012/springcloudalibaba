package com.example.examplebusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ExampleBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleBusinessApplication.class, args);
    }

}
