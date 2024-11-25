package com.myspring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/")
    public String getMain() {
        return "Hello World";
    }

}
