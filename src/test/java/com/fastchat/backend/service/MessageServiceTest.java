package com.fastchat.backend.service;

import com.fastchat.backend.model.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;

//https://medium.com/@Lakshitha_Fernando/integration-testing-bb31676915ae
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    Message message;

    @BeforeEach
    public void setup(){
        message = Message.builder()
                .sender("Mauro")
                .content("content")
                .build();
    }

    @DisplayName("Test 1:Service Save Message Test")
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveEmployeeTest() {

        messageService.save(message);
        Assertions.assertThat(message.getId()).isGreaterThan(0);
    }

    @DisplayName("Test 2: Service Get all messages Test")
    @Test
    @Order(2)
    @Rollback(value = false)
    public void getAllMessagesTest() {

        List<Message> messages = messageService.getAllMessages();

        Assertions.assertThat(messages.size()).isGreaterThan(0);
    }

    @DisplayName("Test 3: Service Delete all messages Test")
    @Test
    @Order(2)
    @Rollback(value = false)
    public void deleteAllMessagesTest() {

        messageService.deleteAll();

        List<Message> messages = messageService.getAllMessages();

        Assertions.assertThat(messages.size()).isEqualTo(0);
    }
}
