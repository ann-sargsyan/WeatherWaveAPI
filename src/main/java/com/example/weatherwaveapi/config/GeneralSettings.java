package com.example.weatherwaveapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "application.settings")
public class GeneralSettings {
    private String serverPort;

    private Map<String, String> openWeatherMap;
    private Map<String, String> yandexMap;

    private RestTemplateProperties restTemplateProperties;
}

