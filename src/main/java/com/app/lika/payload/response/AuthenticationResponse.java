package com.app.lika.payload.response;

import com.app.lika.payload.DTO.Token;
import com.app.lika.payload.DTO.UserSummary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String username;
    private String fullName;
    private List<String> roles;
    private String token;
}
