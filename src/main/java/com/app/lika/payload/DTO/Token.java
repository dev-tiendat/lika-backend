package com.app.lika.payload.DTO;

public class Token {
    private String accessToken;
    private String tokenType = "Bearer";

    public Token(String accessToken) {
        this.accessToken = accessToken;
    }
}
