package com.example.examplebusiness.controller;

import com.example.examplecommon.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/demo")
public class DemoController {

    @Value("${server.port}")
    String port;
    @Autowired
    Environment en;

    @GetMapping("/sayhi")
    public JsonResult sayHi(@Value("${mytest.name1:configEmpty}") String name) {
        String property = en.getProperty("mytest.name1");
        System.out.println("是的，感觉不错！！！！！！！");
        return JsonResult.success("current server port:" + port + ",config name:" + name);
    }

    @GetMapping("/getlist")
    public JsonResult getList() {
        List<String> result = new ArrayList<>();
        result.add("A");
        result.add("B");
        result.add("C");
        result.add(port);
        return JsonResult.success(result);
    }

}



