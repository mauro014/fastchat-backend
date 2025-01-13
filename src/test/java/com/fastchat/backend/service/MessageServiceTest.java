package com.fastchat.backend.service;

import com.fastchat.backend.model.Chat;
import com.fastchat.backend.model.Message;
import com.fastchat.backend.model.User;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;
import java.util.Optional;

//https://medium.com/@Lakshitha_Fernando/integration-testing-bb31676915ae
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    private Message message;

    @BeforeEach
    public void setup(){
        message = Message.builder()
                .sender("sender1")
                .content("content")
                .timestamp(new Date())
                .build();
    }

    @DisplayName("Test: User Creation Test")
    @Test
    @Transactional
    public void createUserTest() {
        User user = userService.createUser("sender1@sender1.com", "sender1");
        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

    @DisplayName("Test: Chat Creation Test")
    @Test
    @Transactional
    public void createChatTest() {
        User user1 = userService.createUser("sender1@sender3.com", "sender3");
        User user2 = userService.createUser("sender2@sender4.com", "sender4");

        Chat chat = chatService.createChat(user1.getEmail(), user2.getEmail());
        Assertions.assertThat(chat.getId()).isGreaterThan(0);
    }

    @DisplayName("Test: Service Save and retrieve Message Test")
    @Test
    public void allCrudTest() {

        User user1 = userService.createUser("sender1@sender1.com", "sender1");
        User user2 = userService.createUser("sender2@sender2.com", "sender2");

        Chat chat = chatService.createChat(user1.getEmail(), user2.getEmail());

        message.setUser(user1);
        message.setChat(chat);
        messageService.save(message);

        Long idMessage = message.getId();
        Assertions.assertThat(message.getId()).isGreaterThan(0);

        Optional<Message> message = messageService.getMessageById(idMessage);
        Assertions.assertThat(message).isPresent()
                .get()
                .extracting(Message::getId)
                .isEqualTo(idMessage);

        List<Message> messages = messageService.getMessagesByChatId(chat.getId());
        if(messages != null && !messages.isEmpty()){
            Assertions.assertThat(messages.get(0).getId()).isEqualTo(idMessage);
        }
        else{
            Assertions.fail("Message with ID " + idMessage + " was not found.");
        }
    }
}
