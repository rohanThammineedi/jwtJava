package com.jt.mgen.repo;

import com.jt.mgen.entity.JiraUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JiraUserRepo extends JpaRepository<JiraUser, Integer> {
    Optional<JiraUser> findByUsername(String username);
}
