package com.kido.auth.user;

import com.kido.auth.common.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public UserDtos.Profile profile(String username) {
        return UserDtos.Profile.from(load(username));
    }

    @Transactional(readOnly = true)
    public List<UserDtos.Profile> all() {
        return users.findAll().stream().map(UserDtos.Profile::from).toList();
    }

    @Transactional
    public void changePassword(String username, UserDtos.ChangePassword request) {
        User user = load(username);
        if (!request.newPassword().equals(request.confirmPassword()))
            throw new BusinessException("PASSWORD_MISMATCH", "New passwords do not match", HttpStatus.BAD_REQUEST);
        if (!encoder.matches(request.currentPassword(), user.getPasswordHash()))
            throw new BusinessException("INVALID_CURRENT_PASSWORD", "Current password is incorrect",
                    HttpStatus.BAD_REQUEST);
        if (encoder.matches(request.newPassword(), user.getPasswordHash()))
            throw new BusinessException("PASSWORD_UNCHANGED", "New password must be different", HttpStatus.BAD_REQUEST);
        user.changePassword(encoder.encode(request.newPassword()));
    }

    private User load(String username) {
        return users.findByUsername(username)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND));
    }
}
