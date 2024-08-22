package com.example.demo.Controller;

import com.example.demo.services.GithubAPIServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GithubApiController {
    private GithubAPIServices githubAPIServices;

    public GithubApiController(GithubAPIServices githubAPIServices) {
        this.githubAPIServices = githubAPIServices;
    }

    @GetMapping("/user/{username}")
    public String getGithubUserData(@PathVariable String username){
        return githubAPIServices.getRepositoryLists(username);
        //return githubAPIServices.getUserData(username);
    }
}
