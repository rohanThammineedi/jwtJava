package com.jt.mgen.repo;

import com.jt.mgen.entity.JiraUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JiraUserRepo extends JpaRepository<JiraUser, Integer> {
}
