package com.esercizio.milestone.ticket_platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/tickets/create").hasAuthority("ADMIN")
                .requestMatchers("/tickets/edit/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/tickets/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ADMIN")
                .requestMatchers("/categories/**").hasAuthority("ADMIN")
                .requestMatchers("/operators/**").hasAuthority("ADMIN")
                .requestMatchers("/tickets", "/tickets/my-tickets").hasAuthority("OPERATOR")
                .requestMatchers("/tickets/*/view").hasAnyAuthority("ADMIN", "OPERATOR")
                .requestMatchers("/tickets/*/update-status").hasAuthority("OPERATOR")
                .requestMatchers("/tickets/*/add-note").hasAnyAuthority("ADMIN", "OPERATOR")
                .requestMatchers("/profile", "/profile/edit").hasAuthority("OPERATOR")
                .requestMatchers("/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/favicon.ico").permitAll()
                .and().formLogin()
                .and().logout()
                .and().csrf().disable();
            
        return http.build();
    }

    @Bean
    DatabaseUserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
