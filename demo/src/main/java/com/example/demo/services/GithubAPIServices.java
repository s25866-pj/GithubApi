package com.example.demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubAPIServices {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private final ObjectMapper objectMapper;

    public GithubAPIServices(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getUserData(String username) {
        String URL ="https://api.github.com/users/"+username;
        return restTemplate.getForObject(URL, String.class);
    }

    public String getRepositoryLists(String username) {
        String name,sha;
        String UserURL ="https://api.github.com/users/"+username+"/repos";

        try {
            JsonNode repositories = objectMapper.readTree(restTemplate.getForObject(UserURL, String.class));
            StringBuilder names = new StringBuilder();
            for (JsonNode repo : repositories) {
                name = String.valueOf(repo.path("name"));
                sha = String.valueOf(getShaData(username,name));
                names.append(repo.path("name").asText()).append("-").append(sha).append("\n");
            }
            // Remove the last comma and space if names is not empty
            if (names.length() > 0) {
                names.setLength(names.length() - 2);
            }
            return names.toString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getShaData(String username, String name) {
        name = name.substring(1,name.length()-1);
        System.out.println(name);
        String ShaURL = "https://api.github.com/repos/"+username+"/"+name+"/commits";
        System.out.println(ShaURL);
        String sha = "";
        try {
            JsonNode response = objectMapper.readTree(restTemplate.getForObject(ShaURL, String.class));
            if (response.isArray() && response.size() > 0) {
                JsonNode firstCommit = response.get(0);
                sha = firstCommit.get("sha").asText(); // Pobierz wartość sha jako tekst
                System.out.println(sha);
            }

            return sha;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
