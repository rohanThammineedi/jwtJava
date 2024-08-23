package com.jt.mgen.service;

//import com.jt.mgen.entity.RefreshToken;

import com.jt.mgen.repo.JiraUserRepo;
//import com.jt.mgen.repo.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JiraUserJwtService {

    @Autowired
    private KeyPair keyPair;


    @Autowired
    private JiraUserRepo jiraUserRepo;

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000)) // 1 minute
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }


    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(keyPair.getPublic())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}