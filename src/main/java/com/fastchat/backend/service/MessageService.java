package com.fastchat.backend.service;

import com.fastchat.backend.model.Chat;
import com.fastchat.backend.model.Message;
import com.fastchat.backend.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

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

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }
}