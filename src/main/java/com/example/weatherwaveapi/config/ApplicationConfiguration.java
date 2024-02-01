package com.example.weatherwaveapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final GeneralSettings generalSettings;



    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(generalSettings.getRestTemplateProperties().connectionTimeout()))
                .setReadTimeout(Duration.ofMillis(generalSettings.getRestTemplateProperties().readTimeout()))
                .build();
    }
}
