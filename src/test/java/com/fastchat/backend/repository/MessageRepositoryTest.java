package com.fastchat.backend.repository;

import com.fastchat.backend.model.Message;
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
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    @DisplayName("Test 1:Save Employee Test")
    @Order(1)
    @Rollback(value = false)
    public void saveMessageTest(){

        Message message = new Message();

        message.setSender("mauro");
        message.setContent("content");
        message.setTimestamp(new Date());

        messageRepository.save(message);

        Assertions.assertThat(message.getId()).isGreaterThan(0);
    }

}
