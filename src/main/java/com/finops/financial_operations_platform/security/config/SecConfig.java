package com.finops.financial_operations_platform.security.config;

import com.finops.financial_operations_platform.security.filter.JwtAuthenticationFilter;
import com.finops.financial_operations_platform.security.service.CustomUserDetailsService;
import com.finops.financial_operations_platform.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/api/transactions/my")
                        .hasAnyRole("USER")

                        .requestMatchers("/api/transactions/all")
                        .hasAnyRole("FINANCE", "AUDITOR", "ADMIN")

                        .requestMatchers("/api/transactions/*/status")
                        .hasAnyRole("FINANCE", "ADMIN")

                        .requestMatchers("/api/reconciliation/**")
                        .hasAnyRole("FINANCE", "ADMIN")

                        .requestMatchers("/api/audit/**")
                        .hasAnyRole("AUDITOR", "ADMIN")

                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider provider) throws Exception{
        return new ProviderManager(provider);
    }

}
