package com.jt.mgen.controller;

import com.jt.mgen.dto.AuthenticationRequest;
import com.jt.mgen.dto.JiraUserApplicationResponseDTO;
import com.jt.mgen.dto.JwtResponseDto;
import com.jt.mgen.dto.RefreshTokenRequest;
import com.jt.mgen.entity.JiraUser;
import com.jt.mgen.entity.RefreshToken;
import com.jt.mgen.exception.JiraUserNotFoundException;
import com.jt.mgen.exception.TokenExpiredException;
import com.jt.mgen.service.JiraUserJwtService;
import com.jt.mgen.service.JiraUserService;
import com.jt.mgen.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jiraUser")
public class JiraUserController {

    private final JiraUserService jiraUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JiraUserJwtService jiraUserJwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public JiraUserController(JiraUserService jiraUserService) {
        this.jiraUserService = jiraUserService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello welcome to JiraUser!";
    }

    @PostMapping("/create")
    public JiraUserApplicationResponseDTO<JiraUser> createJiraUser(@RequestBody JiraUser jiraUser) {
        JiraUser saved = jiraUserService.createJiraUser(jiraUser);
        return new JiraUserApplicationResponseDTO<>(saved, null);
    }

    /*this is login method*/
    @PostMapping("/authenticate")
    public JiraUserApplicationResponseDTO<JwtResponseDto> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            // Generate JWT token
            String accessToken = jiraUserJwtService.generateToken(authenticationRequest.getUsername());
            JwtResponseDto jwtResponse = JwtResponseDto.builder()
                    .accessToken(accessToken)
                    .build();

            // Check if refresh token already exists for the user
            Optional<RefreshToken> existingToken = refreshTokenService.findByJiraUserId(authenticationRequest.getUsername());
            String refreshToken;
            if (existingToken.isPresent()) {
                // Update the existing token
                RefreshToken token = existingToken.get();
                refreshToken = jiraUserJwtService.generateRefreshToken();
                token.setToken(refreshToken);
                token.setExpiryDate(calculateExpiryDate());
                refreshTokenService.save(token);
            } else {
                // Create a new refresh token
                refreshToken = jiraUserJwtService.generateRefreshToken();
                RefreshToken token = new RefreshToken();
                token.setJiraUserId(authenticationRequest.getUsername());
                token.setToken(refreshToken);
                token.setExpiryDate(calculateExpiryDate());
                refreshTokenService.save(token);
            }

            jwtResponse.setRefreshToken(refreshToken);
            return new JiraUserApplicationResponseDTO<>(jwtResponse, null);
        } else {
            throw new JiraUserNotFoundException("Invalid username or password");
        }
    }


    @PostMapping("/refreshToken")
    public JiraUserApplicationResponseDTO<JwtResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getJiraUserId)
                .map(jiraUserId -> {
                    String generatedToken = jiraUserJwtService.generateToken(jiraUserId);
                    JwtResponseDto jwtResponse = JwtResponseDto.builder()
                            .accessToken(generatedToken)
                            .refreshToken(refreshTokenRequest.getToken())
                            .build();
                    return new JiraUserApplicationResponseDTO<>(jwtResponse, null);
                })
                .orElseThrow(() -> new TokenExpiredException("Invalid refresh token, Please login again"));
    }

    @PostMapping("/logout")
    public JiraUserApplicationResponseDTO<String> logout(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(token -> {
                    refreshTokenService.delete(token);
                    return new JiraUserApplicationResponseDTO<>("Logout successful", null);
                })
                .orElseThrow(() -> new TokenExpiredException("Invalid refresh token, Please login again"));
    }

    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public JiraUserApplicationResponseDTO<List<JiraUser>> getAllJiraUsers() {
        List<JiraUser> users = jiraUserService.getAllJiraUsers();
        return new JiraUserApplicationResponseDTO<>(users, null);
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public JiraUserApplicationResponseDTO<JiraUser> getJiraUserById(@PathVariable Integer id) {
        JiraUser user = jiraUserService.getJiraUserById(id);
        return new JiraUserApplicationResponseDTO<>(user, null);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN')")
    public JiraUserApplicationResponseDTO<JiraUser> updateJiraUser(@PathVariable Integer id, @RequestBody JiraUser jiraUser) {
        JiraUser updatedUser = jiraUserService.updateJiraUser(id, jiraUser);
        return new JiraUserApplicationResponseDTO<>(updatedUser, null);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN')")
    public JiraUserApplicationResponseDTO<String> deleteJiraUser(@PathVariable Integer id) {
        String result = jiraUserService.deleteJiraUser(id);
        return new JiraUserApplicationResponseDTO<>(result, null);
    }

    private Date calculateExpiryDate() {
        // Implement your logic to calculate the expiry date
        return new Date(System.currentTimeMillis() + 86400000); // 1 day
    }

}