package com.example.server;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class DeepSeekService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;

    public DeepSeekService(RestTemplate restTemplate, String apiUrl, String apiKey) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public String getChatCompletion(String userMessage) {
        System.out.println("userMessage = " + userMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        String requestBody = String.format("""
        {
            "model": "deepseek-chat",
            "messages": [
                {"role": "system", "content": "请直接给出以下题目的答案，不需要解释或步骤。"},
                {"role": "user", "content": "%s"}
            ],
            "stream": false
        }
        """, userMessage);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

        return response.getBody();
    }
}