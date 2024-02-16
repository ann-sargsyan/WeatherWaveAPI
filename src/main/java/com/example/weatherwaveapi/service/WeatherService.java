package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherResponse;

public interface WeatherService {

     WeatherResponse getWeather(WeatherRequest request);
}
