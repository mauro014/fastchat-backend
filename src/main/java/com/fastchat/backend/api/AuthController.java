package com.fastchat.backend.api;

import com.fastchat.backend.config.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/token")
    public String createToken(@RequestParam String username) {
        return jwtTokenProvider.generateToken(username);
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestParam String token) {
        return jwtTokenProvider.validateToken(token);
    }

}
