package com.jt.mgen.repo;

import com.jt.mgen.entity.JiraUser;
import com.jt.mgen.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByJiraUser(JiraUser user);
}
