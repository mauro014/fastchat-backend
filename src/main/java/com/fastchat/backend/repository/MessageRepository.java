package com.fastchat.backend.repository;

import com.fastchat.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    void deleteAll();

    List<Message> findByChatId(Long chatId);

}
