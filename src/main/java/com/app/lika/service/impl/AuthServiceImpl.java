package com.app.lika.service.impl;

import com.app.lika.exception.APIException;
import com.app.lika.exception.AppException;
import com.app.lika.exception.ExceptionCustomCode;
import com.app.lika.mapper.UserMapper;
import com.app.lika.model.role.Role;
import com.app.lika.model.role.RoleName;
import com.app.lika.model.user.Status;
import com.app.lika.model.user.User;
import com.app.lika.payload.DTO.UserProfile;
import com.app.lika.payload.request.SignInRequest;
import com.app.lika.payload.request.SignUpRequest;
import com.app.lika.payload.response.AuthenticationResponse;
import com.app.lika.repository.RoleRepository;
import com.app.lika.repository.UserRepository;
import com.app.lika.security.JwtTokenProvider;
import com.app.lika.security.UserPrincipal;
import com.app.lika.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    private static final String USER_ROLE_NOT_SET = "User role not set";

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider tokenProvider;

    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsernameOrEmail(), signInRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        String jwt = tokenProvider.generateToken(authentication);
        String fullName = userPrincipal.getFirstName() + userPrincipal.getLastName();

        return new AuthenticationResponse(userPrincipal.getUsername(),fullName, roles, jwt);
    }

    @Override
    public UserProfile signUp(SignUpRequest signUpRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists", ExceptionCustomCode.USERNAME_ALREADY_EXISTS.getCode());
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists", ExceptionCustomCode.EMAIL_ALREADY_EXISTS.getCode());
        }

        RoleName roleRequest = signUpRequest.getRole();
        if (roleRequest == RoleName.ROLE_ADMIN) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Unable to register an Admin account");
        }

        String firstName = signUpRequest.getFirstName().trim();
        String lastName = signUpRequest.getLastName().trim();
        String username = signUpRequest.getUsername().trim().toLowerCase();
        String email = signUpRequest.getEmail().trim().toLowerCase();
        String password = passwordEncoder.encode(signUpRequest.getPassword());
        Status status = roleRequest == RoleName.ROLE_STUDENT ? Status.ACTIVE : Status.INACTIVE;
        User user = new User(username, password, email, firstName, lastName, signUpRequest.getAddress(), signUpRequest.getGender(), signUpRequest.getDateOfBirth(), status);

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(roleRequest)
                .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        user.setRoles(roles);

        return userMapper.entityToUserProfile(userRepository.save(user));
    }
}
