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
    @PreAuthorize("hasAuthority('ROLE_HR')")
    public JiraUser createJiraUser(@RequestBody JiraUser jiraUser) {
        return jiraUserService.createJiraUser(jiraUser);
    }

    @PreAuthorize("hasAuthority('ROLE_HR') or hasAuthority('ROLE_MANAGER')")
    @GetMapping("/getAll")
    public List<JiraUser> getAllJiraUsers() {
        return jiraUserService.getAllJiraUsers();
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_HR')")
    public JiraUser getJiraUserById(@RequestBody Integer id) {
        return jiraUserService.getJiraUserById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_HR')")
    public JiraUser updateJiraUser(@PathVariable Integer id, @RequestBody JiraUser jiraUser) {
        return jiraUserService.updateJiraUser(id, jiraUser);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_HR')")
    public String deleteJiraUser(@PathVariable Integer id) {
        return jiraUserService.deleteJiraUser(id);
    }


}
