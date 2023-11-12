package com.app.lika.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {
    UserDetails loadUserByUsername(String usernameOrEmail);

    UserDetails loadUserById(Long id);
}
