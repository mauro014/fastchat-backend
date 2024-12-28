package com.fastchat.backend.api;

import com.fastchat.backend.model.Message;
import com.fastchat.backend.service.MessageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping("getAllMessages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }
}
