package com.jt.mgen.config;

import com.jt.mgen.service.JiraUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JiraUserDetailsService jiraUserDetailsService;

    @Autowired
    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, JiraUserDetailsService jiraUserDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jiraUserDetailsService = jiraUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/security/nonSecurityMethod", "/jiraUser/hello", "/jiraUser/create").permitAll()
                            .anyRequest().authenticated();
                })
                .userDetailsService(jiraUserDetailsService) // Use your custom userDetailsService
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }
}