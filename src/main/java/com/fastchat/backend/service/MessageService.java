package com.fastchat.backend.service;

import com.fastchat.backend.model.Message;
import com.fastchat.backend.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private SimpMessagingTemplate messagingTemplate;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public void save(Message message) {
        message.setTimestamp(new Date());
        messageRepository.save(message);
    }

    public void deleteAll(){
        messageRepository.deleteAll();
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatId(chatId);
    }

    public void sendPrivateMessage(String recipient, String message) {
        messagingTemplate.convertAndSendToUser(recipient, "/queue/reply", message);
    }
}