package com.example.weatherwaveapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class WeatherService {

    @Value("${openWeatherMap.api.key}")
    private String apiKey;

    @Value("${openWeatherMap.api.url}")
    private String apiUrl;
    public RestTemplate restTemplate = new RestTemplate();

    public Object getWeatherBySelectedCity(String city){
        System.out.println(apiUrl);
        String url = apiUrl + "?q=" + city + "&appid=" + apiKey;
        return restTemplate.getForObject(url, Object.class);
    }
}
