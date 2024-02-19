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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {
    private static final Integer ZERO = 0;
    private static final Integer ONE = 1;
    private static final Integer TWO = 2;
    private static final String REQUEST_MESSAGE = "Request contain serviceApiEnumValue: {}";
    private static final String INVALID_INPUT_MESSAGE = "Invalid input for parameter %s. Please provide a valid value.";

    private final WeatherRouterService weatherRouterService;

    @GetMapping(value = "/params", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WeatherResponse> getWeatherData(@RequestParam(value = "cities", required = false) List<String> cities,
                                                          @RequestParam(value = "zipCodes", required = false) List<String> zipCodes,
                                                          @RequestParam(value = "coordinates", required = false) List<String> coordinates,
                                                          @RequestParam(value = "serviceApi", defaultValue = "OPEN_WEATHER_MAP", required = false) ServiceApiEnum inputOfServiceApi
    ) {

        List<CoordinateWeatherRequest> coordinateRequests = createCoordinateRequests(coordinates);
        List<ZipCodeWeatherRequest> zipCodeWeatherRequests = createZipCodeRequest(zipCodes);

        WeatherRequest weatherRequest = WeatherRequest.builder().cities(cities).zipcode(zipCodeWeatherRequests).coordinates(coordinateRequests).service(inputOfServiceApi).build();
        log.info(REQUEST_MESSAGE, weatherRequest);
        WeatherService weatherService = weatherRouterService.getWeatherBySelectedService(inputOfServiceApi);
        WeatherResponse weatherResponse = weatherService.getWeather(weatherRequest);

        return ResponseEntity.ok(weatherResponse);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(INVALID_INPUT_MESSAGE, ex.getName()));
    }

    private List<CoordinateWeatherRequest> createCoordinateRequests(List<String> coordinates) {
        return Optional.ofNullable(coordinates)
                .map(coords -> IntStream.range(ZERO, coords.size() / TWO)
                        .mapToObj(i -> CoordinateWeatherRequest.builder()
                                .lat(Double.valueOf(coords.get(i * TWO)))
                                .lon(Double.valueOf(coords.get(i * TWO + ONE)))
                                .build())
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    private List<ZipCodeWeatherRequest> createZipCodeRequest(List<String> zipCodes) {
        return Optional.ofNullable(zipCodes)
                .map(zips ->
                        IntStream.range(ZERO, zips.size() / TWO)
                                .mapToObj(i ->
                                        ZipCodeWeatherRequest.builder()
                                                .zipcode(Integer.valueOf(zips.get(i * TWO)))
                                                .country(zips.get(i * TWO + ONE))
                                                .build()
                                )
                                .collect(Collectors.toList())
                )
                .orElse(new ArrayList<>());
    }
}