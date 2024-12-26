package com.fastchat.backend.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {

    public String sendMessage(){
        System.out.println("Send message");
        return "Hi";
    }
}
