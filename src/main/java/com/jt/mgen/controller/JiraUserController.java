package com.jt.mgen.controller;

import com.jt.mgen.entity.JiraUser;
import com.jt.mgen.service.JiraUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jiraUser")
public class JiraUserController {

    private final JiraUserService jiraUserService;

    public JiraUserController(JiraUserService jiraUserService) {
        this.jiraUserService = jiraUserService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello welcome to JiraUser!";
    }

    @PostMapping("/create")
    public JiraUser createJiraUser(@RequestBody JiraUser jiraUser) {
        return jiraUserService.createJiraUser(jiraUser);
    }

    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public List<JiraUser> getAllJiraUsers() {
        return jiraUserService.getAllJiraUsers();
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public JiraUser getJiraUserById(@PathVariable Integer id) {
        return jiraUserService.getJiraUserById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN')")
    public JiraUser updateJiraUser(@PathVariable Integer id, @RequestBody JiraUser jiraUser) {
        return jiraUserService.updateJiraUser(id, jiraUser);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_SITEADMIN') or hasAuthority('ROLE_ADMIN')")
    public String deleteJiraUser(@PathVariable Integer id) {
        return jiraUserService.deleteJiraUser(id);
    }
}