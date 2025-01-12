package com.fastchat.backend.api;

import com.fastchat.backend.model.Chat;
import com.fastchat.backend.repository.UserRepository;
import com.fastchat.backend.service.ChatService;
import com.fastchat.backend.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "${FRONT_URL}", allowCredentials = "true")
@RestController
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/chatsByUser/{email}")
    public List<Chat> getChatsByUser(@PathVariable String email) {
        return chatService.getChatsByUserEmail(email);
    }

    @GetMapping("/chatById/{idChat}")
    public Object chatById(@PathVariable Long idChat) {

        try {
            return chatService.getChatById(idChat);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
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
