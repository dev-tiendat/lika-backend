package com.app.lika.payload.response;

import com.app.lika.payload.DTO.Token;
import com.app.lika.payload.DTO.UserSummary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class AuthenticationResponse {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String token;

    public AuthenticationResponse(Long id, String username, String email, List<String> roles,String accessToken) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
