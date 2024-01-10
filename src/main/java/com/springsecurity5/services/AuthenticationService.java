package com.springsecurity5.services;

import com.springsecurity5.dtos.LoginUserDto;
import com.springsecurity5.dtos.RegisterUserDto;
import com.springsecurity5.entity.User;
import com.springsecurity5.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {

        System.out.println("Inside authenticate method || " + input.getEmail() + " || " + input.getPassword());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        System.out.println("Got here || " + authentication);

        if (authentication == null) {
            throw new RuntimeException("Invalid credentials!");
        }

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found!"));
    }
}