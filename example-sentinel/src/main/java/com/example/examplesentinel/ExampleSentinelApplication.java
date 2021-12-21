package com.example.examplesentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ExampleSentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSentinelApplication.class, args);
    }

}
