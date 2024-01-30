package com.example.weatherwaveapi.controller;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherResponse;
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

    private final WeatherService weatherService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherInSelectedCity(@PathVariable(name = "city") String city) {
        WeatherRequest weatherRequest = WeatherRequest.builder()
                .cities(List.of(city))
                .build();
        WeatherResponse weatherResponse = weatherService.getWeather(weatherRequest);
        return ResponseEntity.ok(weatherResponse);
    }

    @GetMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherInSelectedCities(@RequestParam List<String> cities) {
        WeatherRequest weatherRequest = WeatherRequest.builder()
                .cities(cities)
                .build();
        log.info("Request: {}", weatherRequest);
        WeatherResponse response = weatherService.getWeather(weatherRequest);
        return ResponseEntity.ok(response);
    }

}
