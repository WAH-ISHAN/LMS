package com.Backend.SecureAuth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String role; // optional: "STUDENT" | "LECTURER" | "ADMIN"
}