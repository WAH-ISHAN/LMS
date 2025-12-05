package com.Backend.SecureAuth.controller;



import com.Backend.SecureAuth.config.JwtUtil;
import com.Backend.SecureAuth.dto.AuthResponse;
import com.Backend.SecureAuth.dto.LoginRequest;
import com.Backend.SecureAuth.dto.RegisterRequest;
import com.Backend.SecureAuth.model.Role;
import com.Backend.SecureAuth.model.User;
import com.Backend.SecureAuth.UserRepository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Create default admin on startup
    @PostConstruct
    public void initAdmin() {
        if (userRepository.findByEmail("admin@lms.com").isEmpty()) {
            User admin = User.builder()
                    .fullName("System Admin")
                    .email("admin@lms.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already in use");
        }

        Role role = Role.STUDENT; // default
        if (req.getRole() != null && !req.getRole().isBlank()) {
            try {
                role = Role.valueOf(req.getRole().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                // if invalid role string, keep default STUDENT
            }
        }

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .active(true)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        AuthResponse response = new AuthResponse(
                token,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(), req.getPassword());
            authManager.authenticate(authToken);

            User user = userRepository.findByEmail(req.getEmail())
                    .orElseThrow();

            String token = jwtUtil.generateToken(user.getEmail());

            AuthResponse response = new AuthResponse(
                    token,
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole().name()
            );

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }
}