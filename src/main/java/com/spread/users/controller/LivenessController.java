package com.spread.users.controller;

import com.spread.users.service.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class LivenessController {

    @GetMapping("/liveness")
    public String liveness() {
        return "Hello from Spread Users";
    }
}
