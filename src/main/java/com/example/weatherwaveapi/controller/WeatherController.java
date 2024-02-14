package com.example.weatherwaveapi.controller;

import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import com.example.weatherwaveapi.model.request.OpenWeatherRequest;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherResponse;
import com.example.weatherwaveapi.service.OpenWeatherService;
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

    private final OpenWeatherService openWeatherService;

    @GetMapping(value = "/{city}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherInSelectedCity(@PathVariable(name = "city") String city,
                                                                    @RequestParam(name = "serviceApi", defaultValue = "OPEN_WEATHER_MAP", required = false) ServiceApiEnum inputOfServiceApi) {
        return getWeatherResponse(List.of(city), inputOfServiceApi);
    }

    @GetMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherInSelectedCities(
            @RequestParam(value = "cities") List<String> cities,
            @RequestParam(value = "serviceApi", defaultValue = "OPEN_WEATHER_MAP", required = false) ServiceApiEnum inputOfServiceApi
    ) {
        return getWeatherResponse(cities, inputOfServiceApi);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(INVALID_INPUT_MESSAGE, ex.getName()));
    }

    private ResponseEntity<WeatherResponse> getWeatherResponse(List<String> cities, ServiceApiEnum serviceApi) {
        OpenWeatherRequest weatherRequest = OpenWeatherRequest.builder()
                .cities(cities)
                .service(serviceApi)
                .build();
        log.info(REQUEST_MESSAGE, weatherRequest.service());
        WeatherResponse weatherResponse = openWeatherService.getWeather(weatherRequest);
        return ResponseEntity.ok(weatherResponse);
    }
}
