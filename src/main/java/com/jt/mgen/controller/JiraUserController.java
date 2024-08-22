package com.jt.mgen.controller;

import com.jt.mgen.dto.AuthenticationRequest;
import com.jt.mgen.dto.JwtResponseDTO;
import com.jt.mgen.dto.RefreshTokenRequest;
import com.jt.mgen.entity.JiraUser;
import com.jt.mgen.service.JiraUserJwtService;
import com.jt.mgen.service.JiraUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jiraUser")
public class JiraUserController {

    private final JiraUserService jiraUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JiraUserJwtService jiraUserJwtService;

    public JiraUserController(JiraUserService jiraUserService) {
        this.jiraUserService = jiraUserService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello welcome to JiraUser!";
    }

    @PostMapping("/create")
    public JiraUser createJiraUser(@RequestBody JiraUser jiraUser) {
        return jiraUserService.createJiraUser(jiraUser);
    }

    @PostMapping("/authenticate")
    public JwtResponseDTO authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String accessToken = jiraUserJwtService.generateToken(authenticationRequest.getUsername());
            String refreshToken = jiraUserJwtService.generateRefreshToken(authenticationRequest.getUsername());
            return JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            if (jiraUserJwtService.validateRefreshToken(refreshTokenRequest.getToken())) {
                String username = jiraUserJwtService.extractUsername(refreshTokenRequest.getToken());
                String accessToken = jiraUserJwtService.generateToken(username);
                return JwtResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshTokenRequest.getToken())
                        .build();
            } else {
                throw new RuntimeException("Invalid refresh token");
            }
        } catch (Exception e) {
            // Add detailed logging here
            System.err.println("Error in refreshToken: " + e.getMessage());
            throw e;
        }
    }


    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public List<JiraUser> getAllJiraUsers() {
        return jiraUserService.getAllJiraUsers();
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public JiraUser getJiraUserById(@PathVariable Integer id) {
        return jiraUserService.getJiraUserById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN')")
    public JiraUser updateJiraUser(@PathVariable Integer id, @RequestBody JiraUser jiraUser) {
        return jiraUserService.updateJiraUser(id, jiraUser);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN')")
    public String deleteJiraUser(@PathVariable Integer id) {
        return jiraUserService.deleteJiraUser(id);
    }
}