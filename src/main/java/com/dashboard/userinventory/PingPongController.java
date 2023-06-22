package com.dashboard.userinventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    record PingPong(String ping) {}

    @GetMapping("pingpong")
    public PingPong pingPong() {
        return new PingPong("ping-pong");
    }

}
