package com.example.weatherwaveapi.service.impl;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherForecastResponse;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.service.WeatherService;

public class YandexServiceImpl implements WeatherService {
    @Override
    public WeatherResponse getWeather(WeatherRequest weatherRequest) {
        return null;
    }

    @Override
    public WeatherForecastResponse getForecast(String city) {
        return null;
    }
}
