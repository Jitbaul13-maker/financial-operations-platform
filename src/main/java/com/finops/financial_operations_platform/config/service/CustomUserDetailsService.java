package com.finops.financial_operations_platform.config.service;

import com.finops.financial_operations_platform.user.model.User;
import com.finops.financial_operations_platform.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPasswordHash())
                .roles(user.getRole().toString())
                .build();
    }
}