package com.example.weatherwaveapi.controller;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.service.WeatherService;
import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.weatherwaveapi.serviceapienum.ServiceApiEnum.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {
    private static final String REQUEST_MESSAGE = "Request contain serviceApiEnumValue: {}";

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
    public ResponseEntity<WeatherResponse> getWeatherInSelectedCities(
            @RequestParam(value = "cities") List<String> cities,
            @RequestParam(value = "serviceApi", required = false) ServiceApiEnum serviceApi) {
        WeatherRequest weatherRequest = WeatherRequest.builder()
                .cities(cities)
                .service(validate(serviceApi))
                .build();
        log.info(REQUEST_MESSAGE, weatherRequest.service());
        WeatherResponse response = weatherService.getWeather(weatherRequest);
        return ResponseEntity.ok(response);
    }

    private ServiceApiEnum validate(ServiceApiEnum serviceApi) {
        if( ALL.equals(serviceApi) ||YANDEX.equals(serviceApi) || OPEN_WEATHER_MAP.equals(serviceApi)){
            return serviceApi;
        }
        return ALL;
    }
}
