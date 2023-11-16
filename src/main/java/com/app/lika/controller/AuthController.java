package com.app.lika.controller;

import com.app.lika.model.user.User;
import com.app.lika.payload.request.SignInRequest;
import com.app.lika.payload.request.SignUpRequest;
import com.app.lika.payload.response.APIMessageResponse;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.payload.response.AuthenticationResponse;
import com.app.lika.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        var data = authService.signIn(signInRequest);

        return new ResponseEntity<>(new APIResponse<>("Đăng nhập thành công", data), HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<APIMessageResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        var data = authService.signUp(signUpRequest);

        return new ResponseEntity<>(new APIMessageResponse(Boolean.TRUE, "Đăng ký tài khoản thành công"), HttpStatus.CREATED);
    }
}
