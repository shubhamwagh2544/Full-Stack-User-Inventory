package com.dashboard.userinventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    private static int count=0;

    record PingPong(String ping) {}

    @GetMapping("pingpong")
    public PingPong pingPong() {
        return new PingPong("ping-pong-"+ ++count);
    }

}
