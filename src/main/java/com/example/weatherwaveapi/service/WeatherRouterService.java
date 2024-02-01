package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherRouterService {
    private final WeatherService weatherService;
    private final YandexWeatherService yandexWeatherService;

    public void getWeatherBySelectedService(WeatherRequest weatherRequest){
        ServiceApiEnum serviceType = weatherRequest.service();
        switch (serviceType){
            case OPEN_WEATHER_MAP -> weatherService.getWeather(weatherRequest);
            case YANDEX -> yandexWeatherService.getWeather(weatherRequest);
        }
    }
}
