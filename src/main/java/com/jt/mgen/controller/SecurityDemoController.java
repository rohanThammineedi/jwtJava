package com.jt.mgen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityDemoController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello welcome to Security!";
    }

    @GetMapping("/greeting")
    public String greeting() {
        return "Happy to see you!";
    }

    @GetMapping("/nonSecurityMethod")
    public String nonSecurityMethod() {
        return "This is a non security method";
    }
}
