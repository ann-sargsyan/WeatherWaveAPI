package com.example.weatherwaveapi.controller;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherForecastResponse;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.service.impl.OpenWeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {
    private static final String REQUEST_MESSAGE = "Request contain serviceApiEnumValue: {}";

    private final OpenWeatherServiceImpl openWeatherServiceImpl;

    @GetMapping({"/city"})
    public ResponseEntity<WeatherResponse> getWeather(@RequestBody WeatherRequest request){
        return ResponseEntity.ok(openWeatherServiceImpl.getWeather(request));
    }

    @GetMapping({"/forecast/{city}"})
    public ResponseEntity<WeatherForecastResponse> getForecast(@PathVariable String city){
        return ResponseEntity.ok(openWeatherServiceImpl.getForecast(city));
    }


}
