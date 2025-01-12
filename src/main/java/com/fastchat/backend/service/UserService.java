package com.fastchat.backend.service;

import com.fastchat.backend.model.User;
import com.fastchat.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(String email, String nombre) {

        User user = userRepository.findByEmail(email);

        if (user != null) {
            return user;
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(nombre);
        newUser.setTimestamp(new Date());

        return userRepository.save(newUser);
    }
}
