package com.fastchat.backend.api;

import com.fastchat.backend.model.Chat;
import com.fastchat.backend.model.Message;
import com.fastchat.backend.model.User;
import com.fastchat.backend.repository.UserRepository;
import com.fastchat.backend.service.ChatService;
import com.fastchat.backend.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RestController
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final ChatService chatService;
    private final UserRepository userRepository;

    @GetMapping("/messagesByChatId/{chatId}")
    public List<Message> getMessagesByChatId(@PathVariable Long chatId) {
        return messageService.getMessagesByChatId(chatId);
    }

    @PostMapping("/sendMessage")
    public Object sendMessage(@RequestBody Message message) {

        try {
            User user = userRepository.findByEmail(message.getSender());
            message.setUser(user);

            messageService.save(message);
            return message;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
