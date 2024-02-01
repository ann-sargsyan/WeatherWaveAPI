package com.example.weatherwaveapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GeneralSettings.class)
public class ApplicationConfiguration {
    private final GeneralSettings generalSettings;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(generalSettings.getRestTemplateProperties().getConnectionTimeout()))
                .setReadTimeout(Duration.ofMillis(generalSettings.getRestTemplateProperties().getReadTimeout()))
                .build();
    }

}
