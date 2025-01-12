package com.fastchat.backend.api;

import com.fastchat.backend.model.Chat;
import com.fastchat.backend.model.Message;
import com.fastchat.backend.model.User;
import com.fastchat.backend.service.ChatService;
import com.fastchat.backend.service.MessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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
public class WsController {

    private final MessageService messageService;
    private final ChatService chatService;
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendMessage2")
    @SendTo("/topic/messages2")
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

    @MessageMapping("/notifyChat")
    public void notifyChat(Long idChat) {
        Optional<Chat> chat = chatService.getChatById(idChat);

        if(chat.isPresent()) {
            messagingTemplate.convertAndSend("/topic/chat/" + chat.get().getUser1().getEmail(), chat);
            messagingTemplate.convertAndSend("/topic/chat/" + chat.get().getUser2().getEmail(), chat);
        }
    }

    @MessageMapping("/notifyMessage")
    public void notifyMessage(Long idMessage) {
        Optional<Message> message = messageService.getMessageById(idMessage);

        if(message.isPresent()) {
            messagingTemplate.convertAndSend("/topic/messages/" + message.get().getChat().getId(), message);

            Chat chat = message.get().getChat();
            User user = chat.getUser1().getId().equals(message.get().getUser().getId()) ? chat.getUser2() : chat.getUser1();
            if(user != null) {
                messagingTemplate.convertAndSend("/topic/notification/" + user.getEmail(), message.get().getChat().getId());
            }
        }
    }
}
