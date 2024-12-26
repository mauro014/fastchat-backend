package com.fastchat.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @GetMapping("sendmessage")
    public String sendMessage(){
        return "Hi";
    }
}
