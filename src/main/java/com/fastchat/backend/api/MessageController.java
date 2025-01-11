package com.fastchat.backend.api;

import com.fastchat.backend.model.Chat;
import com.fastchat.backend.model.Message;
import com.fastchat.backend.service.ChatService;
import com.fastchat.backend.service.MessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${FRONT_URL}", allowCredentials = "true")
@RestController
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final ChatService chatService;

    @GetMapping("getAllMessages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/chatsByUser/{email}")
    public List<Chat> getChatsByUser(@PathVariable String email) {
        return chatService.getChatsByUserEmail(email);
    }

    @PostMapping("/createChat")
    public Object createChat(@RequestBody Map<String, String> chatData) {

        try {
            String email1 = chatData.get("email1");
            String email2 = chatData.get("email2");

            return chatService.createChat(email1, email2);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
