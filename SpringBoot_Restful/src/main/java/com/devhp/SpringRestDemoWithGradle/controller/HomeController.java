package com.devhp.SpringRestDemoWithGradle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "Welcom to Restful API";
    }

    @GetMapping("/test")
    public String test(){
        return "Test Api";
    }
    
}
