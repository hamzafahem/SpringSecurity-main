package com.example.authenmedaf.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class pagetest {

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}
