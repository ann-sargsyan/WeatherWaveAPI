package com.example.weatherwaveapi.controller;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.service.ServiceImpl;
import com.example.weatherwaveapi.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {
    private static final String REQUEST_MESSAGE = "Request contain serviceApiEnumValue: {}";

    private final WeatherService weatherService;
    private final ServiceImpl service;

    @GetMapping(value = "/city", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherInSelectedCity(@RequestParam String city) {
        WeatherRequest weatherRequest = WeatherRequest.builder()
                .cities(List.of(city))
                .build();
        log.info(REQUEST_MESSAGE, weatherRequest);
        WeatherResponse weatherResponse = weatherService.getWeather(weatherRequest);
        return ResponseEntity.ok(weatherResponse);
    }

    @GetMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherInSelectedCities(@RequestParam List<String> cities) {
        WeatherRequest weatherRequest = WeatherRequest.builder()
                .cities(cities)
                .build();
        log.info(REQUEST_MESSAGE, weatherRequest);
        WeatherResponse response = weatherService.getWeather(weatherRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "yandex")
    public ResponseEntity<Object> getYandex() {
        log.info(REQUEST_MESSAGE);
        System.out.println(service.getWeather());
        return ResponseEntity.ok(service.getWeather());
    }
}
