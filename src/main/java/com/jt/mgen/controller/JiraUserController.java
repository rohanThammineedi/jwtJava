package com.jt.mgen.controller;

import com.jt.mgen.entity.JiraUser;
import com.jt.mgen.service.JiraUserService;
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

    @GetMapping("/getAll")
    public List<JiraUser> getAllJiraUsers() {
        return jiraUserService.getAllJiraUsers();
    }

    @GetMapping("/getById/{id}")
    public JiraUser getJiraUserById(@RequestBody Integer id) {
        return jiraUserService.getJiraUserById(id);
    }

    @PutMapping("/update/{id}")
    public JiraUser updateJiraUser(@PathVariable Integer id, @RequestBody JiraUser jiraUser) {
        return jiraUserService.updateJiraUser(id, jiraUser);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteJiraUser(@PathVariable Integer id) {
        return jiraUserService.deleteJiraUser(id);
    }


}
