package com.jt.mgen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails employee = User.withUsername("Basant")
                .password(bCryptPasswordEncoder.encode("pwd1"))
                .roles("EMPLOYEE")
                .build();
        UserDetails admin = User.withUsername("Amit")
                .password(bCryptPasswordEncoder.encode("pwd2"))
                .roles("HR")
                .build();
        UserDetails userAdmin = User.withUsername("Parmesh")
                .password(bCryptPasswordEncoder.encode("pwd3"))
                .roles("MANAGER", "HR")
                .build();

        return new InMemoryUserDetailsManager(employee, admin, userAdmin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> {
                    authorize

                            .requestMatchers("/security/nonSecurityMethod","/jiraUser/hello").permitAll()
                            .anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }
}
