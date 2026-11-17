package com.kido.auth.authentication;

import com.kido.auth.user.*;
import com.kido.auth.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager manager;
    private final UserRepository users;
    private final JwtService jwt;

    public AuthController(AuthenticationManager manager, UserRepository users, JwtService jwt) {
        this.manager = manager;
        this.users = users;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = users.findByUsername(request.username()).orElseThrow();
        return new LoginResponse(jwt.create(user), "Bearer", 900);
    }
}
