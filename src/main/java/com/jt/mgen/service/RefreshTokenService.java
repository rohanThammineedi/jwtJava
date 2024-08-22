package com.jt.mgen.service;

import com.jt.mgen.entity.JiraUser;
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
        JiraUser jiraUser = jiraUserRepo.findByUsername(username).orElseThrow();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setJiraUser(jiraUser);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(60000));
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + " Refresh token expired. Please Sign in again");
        }
        return refreshToken;
    }

}
