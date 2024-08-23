package com.jt.mgen.service;

import com.jt.mgen.entity.RefreshToken;
import com.jt.mgen.exception.JiraUserGlobalException;
import com.jt.mgen.repo.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByJiraUserId(String jiraUserId) {
        return refreshTokenRepository.findByJiraUserId(jiraUserId);
    }

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(token);
            throw new JiraUserGlobalException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}