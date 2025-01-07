package com.fastchat.backend.api;

import com.fastchat.backend.config.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String aut_token, HttpServletResponse response) {
        if(true) // Validate aut_token
        {
            String token = jwtTokenProvider.generateToken(aut_token);

            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(86400);

            response.addCookie(jwtCookie);

            return ResponseEntity.ok("profile info");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid auth token");
        }
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestParam String token) {
        return jwtTokenProvider.validateToken(token);
    }

}
