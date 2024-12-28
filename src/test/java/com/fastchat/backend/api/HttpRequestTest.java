package com.fastchat.backend.api;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

//https://medium.com/@Lakshitha_Fernando/spring-boot-unit-testing-for-repositories-controllers-and-services-using-junit-5-and-mockito-def3ff5891be
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Test 1: Get all messages HTTP")
    @Order(1)
    @Rollback(value = false)
    void getAllMessage() throws Exception {
        String localhost = "http://localhost:" + port + "/getAllMessages";
        assertThat(this.restTemplate.getForObject(localhost,
                String.class)).contains("[]");
    }
}
