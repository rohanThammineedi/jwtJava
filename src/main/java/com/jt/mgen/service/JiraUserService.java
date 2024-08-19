package com.jt.mgen.service;

import com.jt.mgen.entity.JiraUser;
import com.jt.mgen.repo.JiraUserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JiraUserService {

    private final JiraUserRepo jiraUserRepo;

    private final BCryptPasswordEncoder passwordEncoder;

    public JiraUserService(JiraUserRepo jiraUserRepo, BCryptPasswordEncoder passwordEncoder) {
        this.jiraUserRepo = jiraUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public JiraUser createJiraUser(JiraUser jiraUser) {
        jiraUser.setPassword(passwordEncoder.encode(jiraUser.getPassword()));
        return jiraUserRepo.save(jiraUser);
    }

    public JiraUser getJiraUserById(Integer id) {
        return jiraUserRepo.findById(id).orElse(null);
    }

    public JiraUser updateJiraUser(Integer id, JiraUser jiraUser) {
        JiraUser existingJiraUser = jiraUserRepo.findById(id).orElse(null);
        if (existingJiraUser == null) {
            return null;
        }
        existingJiraUser.setUsername(jiraUser.getUsername());
        existingJiraUser.setPassword(passwordEncoder.encode(jiraUser.getPassword()));
        existingJiraUser.setEmail(jiraUser.getEmail());
        return jiraUserRepo.save(existingJiraUser);
    }

    public String deleteJiraUser(Integer id) {
        jiraUserRepo.deleteById(id);
        return "JiraUser with id " + id + " has been deleted";
    }

    public List<JiraUser> getAllJiraUsers() {
        return jiraUserRepo.findAll();
    }
}
