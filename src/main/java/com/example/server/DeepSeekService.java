package com.example.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeepSeekService {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public DeepSeekService(RestTemplate restTemplate, String apiUrl, String apiKey, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
    }

    public String getChatCompletion(String userMessage) {
        // 过滤不合法字符
        String sanitizedMessage = sanitizeInput(userMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // 使用 JSON 构造器构造请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "请直接给出以下题目的答案，不需要解释或步骤。输出结果仅包含汉字和字母以及必要的标点符号"),
                Map.of("role", "user", "content", sanitizedMessage)
        ));
        requestBody.put("stream", false);

        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            System.out.println("requestBody 发送的请求体 = " + requestBodyJson);

            HttpEntity<String> request = new HttpEntity<>(requestBodyJson, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            System.out.println("response  =  " + response);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to construct request body", e);
        }
    }

    private String sanitizeInput(String input) {
        // 移除不合法字符，仅保留汉字、字母、数字和必要的标点符号
        return input.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "");
    }
}
