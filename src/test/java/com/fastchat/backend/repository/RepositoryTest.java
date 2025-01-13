package com.fastchat.backend.repository;

import com.fastchat.backend.model.Message;
import com.fastchat.backend.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

//https://medium.com/@Lakshitha_Fernando/spring-boot-unit-testing-for-repositories-controllers-and-services-using-junit-5-and-mockito-def3ff5891be
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class RepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test 1:Save User Test")
    @Rollback(value = false)
    public void saveUserTest(){

        User user = new User();
        user.setName("test");
        user.setEmail("test");
        user.setTimestamp(new Date());

        userRepository.save(user);

        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test 1:Save Message Test")
    @Order(1)
    @Rollback(value = false)
    public void saveMessageTest(){

        User user = new User();
        user.setName("test");
        user.setEmail("test");
        user.setTimestamp(new Date());

        userRepository.save(user);

        Message message = new Message();

        message.setSender("mauro");
        message.setContent("content");
        message.setTimestamp(new Date());
        message.setUser(user);

        messageRepository.save(message);

        Assertions.assertThat(message.getId()).isGreaterThan(0);
    }

}
