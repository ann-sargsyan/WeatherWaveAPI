package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherRouterService {
    private final OpenWeatherService openWeatherService;
    private final YandexWeatherService yandexWeatherService;


    public WeatherService getWeatherService(ServiceApiEnum service) {
        return switch (service) {
            case OPEN_WEATHER_MAP -> openWeatherService;
            case YANDEX -> yandexWeatherService;
            default -> throw new IllegalArgumentException(String.format("Not supported service %s", service));
        };
    }
}
