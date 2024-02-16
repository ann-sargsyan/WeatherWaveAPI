package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherRouterService {
    private static final String NOT_SUPPORTED_MESSAGE = "Not supported service";
    private final OpenWeatherService openWeatherService;
    private final YandexWeatherService yandexWeatherService;


    public WeatherService getWeatherBySelectedService(ServiceApiEnum service) {
        return switch (service) {
            case OPEN_WEATHER_MAP -> openWeatherService;
            case YANDEX -> yandexWeatherService;
            default -> throw new IllegalArgumentException(NOT_SUPPORTED_MESSAGE);
        };
    }
}
