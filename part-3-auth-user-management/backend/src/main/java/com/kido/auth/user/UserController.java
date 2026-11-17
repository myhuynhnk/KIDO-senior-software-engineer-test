package com.kido.auth.user;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/api/users/me")
    public UserDtos.Profile me(Authentication authentication) {
        return service.profile(authentication.getName());
    }

    @PutMapping("/api/users/me/password")
    public UserDtos.Message password(Authentication authentication,
            @Valid @RequestBody UserDtos.ChangePassword request) {
        service.changePassword(authentication.getName(), request);
        return new UserDtos.Message("Password changed. Please sign in again.");
    }

    @GetMapping("/api/admin/users")
    public List<UserDtos.Profile> users() {
        return service.all();
    }
}
