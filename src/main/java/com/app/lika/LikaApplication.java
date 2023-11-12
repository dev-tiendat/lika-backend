package com.app.lika;

import com.app.lika.model.role.RoleName;
import com.app.lika.security.JwtAuthenticationFilter;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.TimeZone;

@SpringBootApplication
public class LikaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LikaApplication.class, args);
    }

    @PostConstruct
    void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

}
