package com.kido.auth.security;

import com.kido.auth.user.*;
import com.kido.auth.user.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DemoDataConfig {
    @Bean
    CommandLineRunner demoUsers(UserRepository users, RoleRepository roles, PasswordEncoder encoder,
            @Value("${app.demo-admin-password:}") String adminPass,
            @Value("${app.demo-user-password:}") String userPass) {
        return args -> {
            Role admin = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(new Role("ROLE_ADMIN")));
            Role user = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(new Role("ROLE_USER")));
            if (!users.existsByUsername("admin")) {
                if (adminPass.length() < 8)
                    throw new IllegalStateException("Set DEMO_ADMIN_PASSWORD (8+ characters)");
                users.save(new User("admin", "admin@example.test", "Demo Administrator", encoder.encode(adminPass),
                        admin));
            }
            if (!users.existsByUsername("user")) {
                if (userPass.length() < 8)
                    throw new IllegalStateException("Set DEMO_USER_PASSWORD (8+ characters)");
                users.save(new User("user", "user@example.test", "Demo User", encoder.encode(userPass), user));
            }
        };
    }
}
