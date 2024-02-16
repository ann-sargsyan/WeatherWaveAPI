package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.config.GeneralSettings;
import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import com.example.weatherwaveapi.util.urlbuilder.UrlBuilderForWeather;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public final class WeatherRouterService {

    private final UrlBuilderForWeather urlBuilderForWeather;
    private final GeneralSettings generalSettings;
    private final RestTemplate restTemplate;

    public WeatherService getWeatherBySelectedService(ServiceApiEnum service) {
        return switch (service) {
            case OPEN_WEATHER_MAP -> new OpenWeatherService(restTemplate, urlBuilderForWeather);
            case YANDEX -> new YandexWeatherService(restTemplate, generalSettings, urlBuilderForWeather);
            default -> null;
        };
    }
}
