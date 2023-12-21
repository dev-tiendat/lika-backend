//package com.app.lika.config;
//
//import jakarta.servlet.MultipartConfigElement;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.servlet.MultipartConfigFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.unit.DataSize;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class WebMvcConfig implements WebMvcConfigurer {
//
////    @Value("cors.allowedOrings")
////    private String allowedOrigins;
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        final long MAX_AGE_SECS = 3600;
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("*")
//                        .exposedHeaders("*")
//                        .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
//                        .maxAge(MAX_AGE_SECS)
//                ;
//            }
//
//        };
//    }
//
//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setMaxFileSize(DataSize.ofBytes(100000000L));
//        factory.setMaxRequestSize(DataSize.ofBytes(100000000L));
//        return factory.createMultipartConfig();
//    }
//}
