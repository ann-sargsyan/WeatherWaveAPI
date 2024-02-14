package com.example.weatherwaveapi.controller;

import com.example.weatherwaveapi.model.request.OpenWeatherRequest;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherResponse;
import com.example.weatherwaveapi.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {
    private static final String REQUEST_MESSAGE = "Request contain serviceApiEnumValue: {}";

    private final OpenWeatherService openWeatherService;

    @GetMapping(value = "/city", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherInSelectedCity(@RequestParam String city) {
        OpenWeatherRequest weatherRequest = OpenWeatherRequest.builder()
                .cities(List.of(city))
                .build();
        log.info(REQUEST_MESSAGE, weatherRequest);
        WeatherResponse weatherResponse = openWeatherService.getWeather(weatherRequest);
        return ResponseEntity.ok(weatherResponse);
    }

    @GetMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherInSelectedCities(@RequestParam List<String> cities) {
        OpenWeatherRequest weatherRequest = OpenWeatherRequest.builder()
                .cities(cities)
                .build();
        log.info(REQUEST_MESSAGE, weatherRequest);
        WeatherResponse response = openWeatherService.getWeather(weatherRequest);
        return ResponseEntity.ok(response);
    }
}