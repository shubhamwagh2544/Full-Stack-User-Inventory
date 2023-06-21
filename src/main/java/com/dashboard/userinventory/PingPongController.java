package com.dashboard.userinventory;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    record PingPong(String pingpong) {}

    @GetMapping("/ping-pong")
    public PingPong pingpong() {
        return new PingPong("ping-pong");
    }
}
