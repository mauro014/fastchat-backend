package com.fastchat.backend.api;

import com.fastchat.backend.model.Message;
import com.fastchat.backend.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

//https://spring.io/guides/gs/messaging-stomp-websocket
@Controller
public class ChatController {

    private final MessageService messageService;

    public ChatController(MessageService messageService){
        this.messageService = messageService;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        messageService.save(message);
        return message;
    }

    @MessageMapping("deleteAllMessages")
    @SendTo("/topic/messages")
    public String deleteAll(){
        messageService.deleteAll();
        return  "CLEAR";
    }
}
