package com.fastchat.backend.service;

import com.fastchat.backend.model.Message;
import com.fastchat.backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

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
}