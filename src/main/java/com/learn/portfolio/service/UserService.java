package com.learn.portfolio.service;


import com.learn.portfolio.model.User;
import com.learn.portfolio.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;



public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);
}