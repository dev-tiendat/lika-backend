package com.app.lika.service;

import com.app.lika.model.user.User;
import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.request.SignInRequest;
import com.app.lika.payload.request.SignUpRequest;
import com.app.lika.payload.response.APIResponse;
import com.app.lika.payload.response.AuthenticationResponse;

public interface AuthService {

    AuthenticationResponse signIn(SignInRequest signInRequest);

    UserProfile signUp(SignUpRequest signUpRequest);
}
