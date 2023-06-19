package com.olegpruh.notificationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/websocket/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> {
                    try {
                        form.init(http);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
        UserDetails user1 = User.builder()
                .username("user1")
                .password(passwordEncoder.encode("user1"))
                .roles("USER")
                .build();
        UserDetails user2 = User.builder()
                .username("user2")
                .password(passwordEncoder.encode("user2"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
}
