package com.app.lika.controller;

import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.request.SignInRequest;
import com.app.lika.payload.request.SignUpRequest;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.payload.response.AuthenticationResponse;
import com.app.lika.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/signIn")
    public ResponseEntity<APIResponse<AuthenticationResponse>> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        AuthenticationResponse data = authService.signIn(signInRequest);
        APIResponse<AuthenticationResponse> response = new APIResponse<>("Login successful !", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<APIResponse<UserProfile>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserProfile data = authService.signUp(signUpRequest);
        APIResponse<UserProfile> response = new APIResponse<>("Register account successful !", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
