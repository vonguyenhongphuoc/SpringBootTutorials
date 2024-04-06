package com.devhp.SpringRestDemoWithGradle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
public class AccountController {
    
    @GetMapping("/")
    public String demo() {
        return "Welcom to Restful API";
    }

    @GetMapping("/test")
    @Tag(name = "Test", description = "The test API.")
    @SecurityRequirement(name = "devhp-demo-api")
    public String test(){
        return "Test Api";
    }
    
}
