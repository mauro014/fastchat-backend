package com.fastchat.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
@Service
public class GoogleProfileService {

    WebClient.Builder webClientBuilder;

    public String getGoogleProfile(String access_token) {
        // Prepare the API URL
        String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + access_token;

        WebClient webClient = webClientBuilder
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer " + access_token)
                .defaultHeader("Accept", "application/json")
                .build();

        String profile =
                webClient.get()
                .retrieve()
                        .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                                response -> {
                                    // Log any errors related to the response status
                                    System.err.println("Error response received: " + response.statusCode());
                                    return response.createException();
                                })
                .bodyToMono(String.class)
                .block();

        return profile;
    }
}
