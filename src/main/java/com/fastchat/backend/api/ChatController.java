package com.fastchat.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @GetMapping("sendmessage")
    public String sendMessage(){
        System.out.println("Send message");
        return "Hi";
    }
}
