package com.example.weatherwaveapi.controller;

import com.example.weatherwaveapi.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/{city}")
    public Object getWeatherInSelectedCity(@PathVariable(name = "city") String city){
        return weatherService.getWeatherBySelectedCity(city);
    }
}
