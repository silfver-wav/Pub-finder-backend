package com.pubfinder.pubfinder.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    String password;
}
