package com.fastchat.backend.service;

import com.fastchat.backend.model.Chat;
import com.fastchat.backend.model.User;
import com.fastchat.backend.repository.ChatRepository;
import com.fastchat.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private UserRepository userRepository;

    public Chat createChat(String email1, String email2) {

        User user1 = userRepository.findByEmail(email1);
        User user2 = userRepository.findByEmail(email2);

        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException(
                    "The user " + (user1 == null ? email1: email2) + " does not exist.");
        }

        Optional<Chat> chat1 = chatRepository.findByUser1AndUser2(user1, user2);
        Optional<Chat> chat2 = chatRepository.findByUser1AndUser2(user2, user1);

        if(chat1.isPresent() || chat2.isPresent()){
            throw new IllegalArgumentException("Chat already exist.");
        }

        Chat chat = new Chat();
        chat.setTimestamp(new Date());
        chat.setUser1(user1);
        chat.setUser2(user2);

        return chatRepository.save(chat);
    }

    public List<Chat> getChatsByUserEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        return chatRepository.findByUser1OrUser2(user, user);
    }

    public Optional<Chat> getChatById(Long id) {
        return chatRepository.findById(id);
    }


}
