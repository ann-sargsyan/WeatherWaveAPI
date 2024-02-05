package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.util.urlbuilder.OpenWeatherUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {
    private final OpenWeatherUrlBuilder urlBuilder;
    private final RestTemplate restTemplate;

    public Object getWeatherBySelectedCity(String city) {
        String url = urlBuilder.buildWeatherUrl(city);
        return restTemplate.getForObject(url, Object.class);
    }
}

