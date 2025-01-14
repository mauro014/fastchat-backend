package com.fastchat.backend.api;

import com.fastchat.backend.model.Chat;
import com.fastchat.backend.service.GoogleProfileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;

//https://www.baeldung.com/mockito-junit-5-extension
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    protected GoogleProfileService googleProfileService;

    protected final String mockAccessToken1 = "mockAccessToken1";
    protected final String mockAccessToken2 = "mockAccessToken2";
    protected final String mockEmail1 = "user1@user1.com";
    protected final String mockEmail2 = "user2@user2.com";
    protected String apiUrl = "http://localhost:";

    @BeforeEach
    void setup() {
        String mock1GoogleProfileJson = "{\"email\": \"" + mockEmail1 + "\", \"name\": \"Test User\" }";
        Mockito.when(googleProfileService.getGoogleProfile(eq(mockAccessToken1)))
                .thenReturn(mock1GoogleProfileJson);

        String mock2GoogleProfileJson = "{\"email\": \"" + mockEmail2 + "\", \"name\": \"Test User\" }";
        Mockito.when(googleProfileService.getGoogleProfile(eq(mockAccessToken2)))
                .thenReturn(mock2GoogleProfileJson);
        apiUrl = apiUrl + port;
    }

    @DisplayName("Test: Validate Token and Create Cookie")
    @Test
    void validateToken_andReturnJwtCookie() throws Exception {

        String url = apiUrl + "/auth/validateToken";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url, HttpMethod.POST, createHttpRequestAuth(mockAccessToken1), String.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).contains(mockEmail1);

        String jwt_cookie = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        Assertions.assertThat(jwt_cookie).contains("jwt");
    }

    @DisplayName("Test: Create and retrieve chats")
    @Test
    void createAndRetrieveChats(){

        String JWTCookie1 = autenticateUser(mockAccessToken1);
        String JWTCookie2 = autenticateUser(mockAccessToken2);

        Map<String, String> chatData = new HashMap<>();
        chatData.put("email1", mockEmail1);
        chatData.put("email2", mockEmail2);

        //Create chat
        String urlCreate = apiUrl + "/chat/create";

        HttpHeaders headers = createHeadersBasic();
        headers.add(HttpHeaders.COOKIE, JWTCookie1);

        HttpEntity<Map<String, String>> requestCreate = new HttpEntity<>(chatData, headers);

        ResponseEntity<Chat> responseCreate = restTemplate.exchange(
                urlCreate, HttpMethod.POST, requestCreate, Chat.class);

        Assertions.assertThat(responseCreate.getStatusCode()).isEqualTo(HttpStatus.OK);
        Chat chat = responseCreate.getBody();

        Assertions.assertThat(chat.getUser1().getEmail()).isEqualTo(mockEmail1);
        Assertions.assertThat(chat.getUser2().getEmail()).isEqualTo(mockEmail2);

        //Retrieve chat
        String urlChatById = apiUrl + "/chat/get/" + chat.getId();
        RequestEntity<Void> requestChatById = new RequestEntity<>(headers, HttpMethod.GET, URI.create(urlChatById));
        ResponseEntity<Chat> responseGetChat = restTemplate.exchange(
                requestChatById, Chat.class);

        Assertions.assertThat(responseGetChat.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseGetChat.getBody().getId()).isEqualTo(chat.getId());

        //Retrieve all chats by user
        String urlChatByUser = apiUrl + "/chat/byUser/" + mockEmail2;
        RequestEntity<Void> requestByUser = new RequestEntity<>(headers, HttpMethod.GET, URI.create(urlChatByUser));
        //https://medium.com/@rtj1857/how-to-use-parametizedtypereference-in-spring-boot-for-rest-api-calls-c3168a46f4df
        ResponseEntity<List<Chat>> responseByUser = restTemplate.exchange(
                requestByUser, new ParameterizedTypeReference<>() {});

        Assertions.assertThat(responseByUser.getStatusCode()).isEqualTo(HttpStatus.OK);
        //Check if the chat is in the list.
        List<Chat> chats = responseByUser.getBody();
        Chat foundChat = null;
            if(chats != null){
            for (Chat chatItem : chats) {
                if (chatItem.getId().equals(chat.getId())) {
                    foundChat = chatItem;
                }
            }
        }
        Assertions.assertThat(foundChat.getId()).isEqualTo(chat.getId());

    }

    private String autenticateUser(String mockAccessToken){
        String url = apiUrl + "/auth/validateToken";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url, HttpMethod.POST, createHttpRequestAuth(mockAccessToken), String.class);
        return responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }

    private HttpEntity<String> createHttpRequestAuth(String mockAccessToken) {

        String requestBody = "{\"accessToken\": \"" + mockAccessToken + "\"}";
        HttpHeaders headers = createHeadersBasic();

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpHeaders createHeadersBasic(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.ORIGIN, "http://localhost:3000");
        return headers;
    }
}
