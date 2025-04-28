package com.example.config;
import com.example.server.DeepSeekService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DeepSeekService deepSeekService(RestTemplate restTemplate) {
        return new DeepSeekService(restTemplate, apiUrl, apiKey);
    }
}