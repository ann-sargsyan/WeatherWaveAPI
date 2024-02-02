package com.example.weatherwaveapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.settings")
public class GeneralSettings {

    private OpenWeatherApi openWeatherApi;
    private YandexApi yandexApi;
    private RestTemplateProperties restTemplateProperties;
}

