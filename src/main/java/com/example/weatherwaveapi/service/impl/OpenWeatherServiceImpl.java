package com.example.weatherwaveapi.service.impl;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.*;
import com.example.weatherwaveapi.model.response.openweather.WeatherOpenApiContainer;
import com.example.weatherwaveapi.service.WeatherService;
import com.example.weatherwaveapi.util.urlbuilder.OpenWeatherUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.GET;

@RequiredArgsConstructor
@Service
@Slf4j
public class OpenWeatherServiceImpl implements WeatherService {
    private static final String EXCEPTION_MESSAGE = "HTTP error occurred while processing request. Exception message: {}";
    private static final String ERROR_MESSAGE = "Failed to retrieve weather data ";
    private static final String STRING_MESSAGE_FORMAT = "Search string: %s and error: %s";

    private final RestTemplate restTemplate;
    private final OpenWeatherUrlBuilder urlBuilder;

    @Override
    public WeatherResponse getWeather(WeatherRequest request) {
        List<WeatherApiResponse> weatherResponses = new ArrayList<>();

        if (request.cities() != null && !request.cities().isEmpty()) {
            weatherResponses = request.cities().stream()
                    .map(this::getWeatherBySelectedCity)
                    .map(this::convertContainer)
                    .collect(Collectors.toList());
        } else if (request.zipCodeCountryMap() != null && !request.zipCodeCountryMap().isEmpty()) {
            weatherResponses = request.zipCodeCountryMap().entrySet().stream()
                    .map(entry -> getWeatherByZipCode(entry.getKey(), entry.getValue()))
                    .map(this::convertContainer)
                    .collect(Collectors.toList());
        }

        return WeatherResponse.builder()
                .responses(weatherResponses)
                .build();
    }

    @Override
    public WeatherForecastResponse getForecast(String city) {
        WeatherOpenApiContainer container = getForecastContainerByCity(city);
        if (container.success() == null) {
            return WeatherForecastResponse.builder()
                    .success(true)
                    .forecastData(container.forecastContainer())
                    .city(container.cityDetails().name())
                    .country(container.cityDetails().country())
                    .build();
        } else {
            return WeatherForecastResponse.builder()
                    .success(false)
                    .build();
        }
    }
    
    private WeatherOpenApiContainer getWeatherBySelectedCity(String city) {
        String url = urlBuilder.buildWeatherUrl(city);
        return getWeatherOpenApiContainer(url, String.format(STRING_MESSAGE_FORMAT, city, ERROR_MESSAGE));
    }


    private WeatherOpenApiContainer getWeatherByZipCode(Integer zipcode, String country) {
        String url = Optional.of(country)
                .map(c -> urlBuilder.buildWeatherUrlForZipcode(zipcode, c))
                .orElse(urlBuilder.buildWeatherUrl(zipcode.toString()));

        return getWeatherOpenApiContainer(url, String.format(STRING_MESSAGE_FORMAT, zipcode, ERROR_MESSAGE));
    }

    private WeatherOpenApiContainer getForecastContainerByCity(String city) {
        String url = urlBuilder.buildForecastUrlForCity(city);
        return getWeatherOpenApiContainer(url, String.format(STRING_MESSAGE_FORMAT, city, ERROR_MESSAGE));
    }

    private WeatherOpenApiContainer getWeatherOpenApiContainer(String url, String format) {
        try {
            ResponseEntity<WeatherOpenApiContainer> responseEntity = restTemplate.exchange(
                    url,
                    GET,
                    new HttpEntity<>(new HttpHeaders()),
                    new ParameterizedTypeReference<>() {
                    }
            );
            return responseEntity.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(EXCEPTION_MESSAGE, e.getMessage());
            return WeatherOpenApiContainer.builder()
                    .success(false)
                    .errorMessage(format)
                    .build();
        }
    }

    private WeatherApiResponse convertContainer(WeatherOpenApiContainer container) {
        if (container.success() == null) {
            return WeatherApiResponse.builder()
                    .success(true)
                    .temperature(container.weatherMetrics().temp())
                    .city(container.cityName())
                    .country(container.sunActivityInfo().country())
                    .weatherDescription(container.weather().get(0).description())
                    .build();
        } else {
            return WeatherApiResponse.builder()
                    .success(false)
                    .build();
        }
    }
}
