package com.fastchat.backend.api;

import com.fastchat.backend.config.JwtTokenProvider;
import com.fastchat.backend.model.User;
import com.fastchat.backend.service.GoogleProfileService;
import com.fastchat.backend.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "${FRONT_URL}", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final GoogleProfileService googleProfileService;

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody JsonNode requestBody, HttpServletResponse response) throws JsonProcessingException {

        String accessToken = requestBody.get("accessToken").asText();
        String profile = googleProfileService.getGoogleProfile(accessToken);

        if(profile != null)
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(profile);

            User user = userService.createUser(
                    jsonNode.get("email").asText(),
                    jsonNode.get("name").asText());

            if(user != null) {
                String token = jwtTokenProvider.generateToken(accessToken);
                response.addHeader("Set-Cookie", "jwt=" + token + "; Path=/; Max-Age=86400; Secure; HttpOnly; SameSite=None");
                return ResponseEntity.ok(profile);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid auth token");
    }

}
