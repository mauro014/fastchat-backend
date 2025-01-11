package com.fastchat.backend.api;

import com.fastchat.backend.model.Chat;
import com.fastchat.backend.model.Message;
import com.fastchat.backend.service.ChatService;
import com.fastchat.backend.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

//https://spring.io/guides/gs/messaging-stomp-websocket
@Controller
@AllArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final ChatService chatService;
    private SimpMessagingTemplate messagingTemplate;

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

    @MessageMapping("/createChat")
    @SendTo("/topic/chat/{userId}")
    public Chat createChat(String chatData) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(chatData);

        return chatService.createChat(
            jsonNode.get("email1").asText(),
            jsonNode.get("email2").asText()
        );
    }

    @MessageMapping("/messages/{chatId}")
    @SendTo("/topic/messages")
    public List<Message> getMessagesByChatId(@PathVariable Long chatId) {
        return messageService.getMessagesByChatId(chatId);
    }

    @MessageMapping("/newChat")
    public void handleChat(Long idChat) {
        Optional<Chat> chat = chatService.getChatById(idChat);

        if(chat.isPresent()) {
            messagingTemplate.convertAndSend("/topic/chat/" + chat.get().getUser1().getEmail(), chat);
            messagingTemplate.convertAndSend("/topic/chat/" + chat.get().getUser2().getEmail(), chat);
        }
    }

}
