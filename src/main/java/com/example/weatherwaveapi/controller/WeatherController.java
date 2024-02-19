package com.example.weatherwaveapi.controller;

import com.example.weatherwaveapi.model.request.*;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.service.WeatherRouterService;
import com.example.weatherwaveapi.service.WeatherService;
import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {
    private static final String REQUEST_MESSAGE = "Request contain serviceApiEnumValue: {}";
    private static final String INVALID_INPUT_MESSAGE = "Invalid input for parameter %s. Please provide a valid value.";

    private final WeatherRouterService weatherRouterService;

    @GetMapping(value = "/params", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherData(@RequestParam(value = "cities", required = false) List<String> cities,
                                                          @RequestParam(value = "zipCodes", required = false) List<String> zipCodes,
                                                          @RequestParam(value = "coordinates", required = false) List<String> coordinates,
                                                          @RequestParam(value = "serviceApi", defaultValue = "OPEN_WEATHER_MAP", required = false) ServiceApiEnum inputOfServiceApi
    ) {

        WeatherRequest weatherRequest = WeatherRequest.builder().cities(cities).zipcode(zipCodes).coordinates(coordinates).service(inputOfServiceApi).build();
        log.info(REQUEST_MESSAGE, weatherRequest);
        WeatherService weatherService = weatherRouterService.getWeatherService(inputOfServiceApi);
        WeatherResponse weatherResponse = weatherService.getWeather(weatherRequest);

        return ResponseEntity.ok(weatherResponse);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(INVALID_INPUT_MESSAGE, ex.getName()));
    }
}