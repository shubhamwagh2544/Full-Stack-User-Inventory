package com.dashboard.userinventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingPongController {
    @GetMapping
    public String ping() {
        return "ping-pong";
    }
}
