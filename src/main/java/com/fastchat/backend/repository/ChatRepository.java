package com.fastchat.backend.repository;

import com.fastchat.backend.model.Chat;
import com.fastchat.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByUser1OrUser2(User user1, User user2);

    Optional<Chat> findByUser1AndUser2(User user1, User user2);
}
