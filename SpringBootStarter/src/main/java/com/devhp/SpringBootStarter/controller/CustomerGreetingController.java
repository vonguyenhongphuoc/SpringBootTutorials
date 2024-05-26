package com.devhp.SpringBootStarter.controller;

import com.devhp.SpringBootStarter.model.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/v1")
@Slf4j
public class CustomerGreetingController {

    private static final String greetingTemplate = "Hellom %s %s";
    private final AtomicLong counter = new AtomicLong();


    
    public CustomerGreetingController() {
        log.info("init CustomerGreetingController class");
    }

  

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "gender", defaultValue = "0") boolean gender,
            @RequestParam(value = "customerName", defaultValue = "Customer") String customerName) {
        return Greeting.builder().id(counter.incrementAndGet())
                .content(String.format(greetingTemplate, gender ? "Mr." : "Ms.", customerName)).build();
    }

}
