package com.kido.auth.user;

import jakarta.validation.constraints.*;

public final class UserDtos {
    private UserDtos() {
    }

    public record Profile(Long id, String username, String email, String fullName, String role) {
        public static Profile from(User u) {
            return new Profile(u.getId(), u.getUsername(), u.getEmail(), u.getFullName(), u.getRole().getName());
        }
    }

    public record ChangePassword(@NotBlank String currentPassword,
            @NotBlank @Size(min = 8, max = 72) String newPassword, @NotBlank String confirmPassword) {
    }

    public record Message(String message) {
    }
}
