package com.finops.financial_operations_platform.user.service;

import com.finops.financial_operations_platform.Exceptions.UsernameNotFoundException;
import com.finops.financial_operations_platform.config.service.JwtService;
import com.finops.financial_operations_platform.user.Dto.LoginRequest;
import com.finops.financial_operations_platform.user.Dto.LoginResponse;
import com.finops.financial_operations_platform.user.Dto.RegisterRequest;
import com.finops.financial_operations_platform.user.Dto.RegisterResponse;
import com.finops.financial_operations_platform.user.enums.Role;
import com.finops.financial_operations_platform.user.model.User;
import com.finops.financial_operations_platform.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())){
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setCustomerId("CUST_" + UUID.randomUUID());

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getUsername(),
                savedUser.getCustomerId()
        );
    }

    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }
}
