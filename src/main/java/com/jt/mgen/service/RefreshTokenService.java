package com.jt.mgen.service;

import com.jt.mgen.entity.RefreshToken;
import com.jt.mgen.repo.JiraUserRepo;
import com.jt.mgen.repo.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JiraUserRepo jiraUserRepo;


    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .jiraUser(jiraUserRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(120000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired. Please login again");
        }
        return refreshToken;
    }
}
