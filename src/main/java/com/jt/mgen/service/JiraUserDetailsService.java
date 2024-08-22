package com.jt.mgen.service;

import com.jt.mgen.entity.JiraUser;
import com.jt.mgen.repo.JiraUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JiraUserDetailsService implements UserDetailsService {

    @Autowired
    private JiraUserRepo jiraUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<JiraUser> jiraUserOptional = jiraUserRepo.findByUsername(username);
        if (jiraUserOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return jiraUserOptional.map(JiraUserDetails::new).get();
    }

    public void saveUser(JiraUser jiraUser) {
        jiraUser.setPassword(passwordEncoder.encode(jiraUser.getPassword()));
        jiraUserRepo.save(jiraUser);
    }
}