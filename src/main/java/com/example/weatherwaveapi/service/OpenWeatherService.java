package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.model.request.OpenWeatherRequest;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherApiResponse;
import com.example.weatherwaveapi.model.response.weatherapi.forecast.WeatherForecastResponse;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherResponse;
import com.example.weatherwaveapi.model.response.weatherapi.WeatherOpenApiContainer;
import com.example.weatherwaveapi.util.exception.InvalidZipCodeException;
import com.example.weatherwaveapi.util.urlbuilder.OpenWeatherUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
public class OpenWeatherService {
    private static final String EXCEPTION_MESSAGE = "HTTP error occurred while processing request. Exception message: {}";
    private static final String WEATHER_ERROR_MESSAGE = "Failed to retrieve weather data";
    private static final String FORECAST_ERROR_MESSAGE = "Failed to retrieve forecast data";
    private static final String STRING_MESSAGE_FORMAT = "Search string: %s, error: %s";
    private static final String EMPTY_ZIPCODE_EXCEPTION_MESSAGE = "ZIP code cannot be null";
    private static final String INVALID_ZIPCODE_EXCEPTION_MESSAGE = "ZIP code must be 5 digits long";


    private final RestTemplate restTemplate;
    private final OpenWeatherUrlBuilder urlBuilder;

    public WeatherResponse getWeather(OpenWeatherRequest request) {
        List<WeatherApiResponse> weatherResponses = new ArrayList<>();

        if (!CollectionUtils.isEmpty(request.cities())) {
            weatherResponses = request.cities().stream()
                    .map(this::getWeatherBySelectedCity)
                    .map(this::convertContainer)
                    .collect(Collectors.toList());
        }

        else if (!CollectionUtils.isEmpty(request.zipcode())) {
            weatherResponses = request.zipcode().stream()
                    .map(x -> getWeatherByZipCode(x.zipcode(), x.country()))
                    .map(this::convertContainer)
                    .collect(Collectors.toList());
        }

        return WeatherResponse.builder()
                .responses(weatherResponses)
                .build();
    }

    public WeatherForecastResponse getForecast(String city) {
        WeatherOpenApiContainer container = getForecastContainerByCity(city);
        return Optional.ofNullable(container.errorMessage())
                .map(error -> WeatherForecastResponse.builder()
                        .success(false)
                        .errorMessage(error)
                        .build())
                .orElseGet(() -> WeatherForecastResponse.builder()
                    .forecastData(container.forecastContainer())
                    .city(container.cityDetails().name())
                    .country(container.cityDetails().country())
                    .build());
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
                    .errorMessage(format)
                    .build();
        }
    }

    private WeatherOpenApiContainer getWeatherBySelectedCity(String city) {
        String url = urlBuilder.buildWeatherUrl(city);
        return getWeatherOpenApiContainer(url, String.format(STRING_MESSAGE_FORMAT, city, WEATHER_ERROR_MESSAGE));
    }

    private WeatherOpenApiContainer getWeatherByZipCode(Integer zipcode, String country) {
        validateZipCode(zipcode);
        String url = Optional.ofNullable(country)
                .map(c -> urlBuilder.buildOpenWeatherUrlForZipcode(zipcode, c))
                .orElse(urlBuilder.buildWeatherUrl(zipcode.toString()));

        return getWeatherOpenApiContainer(url, String.format(STRING_MESSAGE_FORMAT, zipcode, WEATHER_ERROR_MESSAGE));
    }

    private WeatherOpenApiContainer getForecastContainerByCity(String city) {
        String url = urlBuilder.buildForecastUrlForCity(city);
        return getWeatherOpenApiContainer(url, String.format(STRING_MESSAGE_FORMAT, city, FORECAST_ERROR_MESSAGE));
    }

    private WeatherApiResponse convertContainer(WeatherOpenApiContainer container) {
        return Optional.ofNullable(container.errorMessage())
                .map(error -> WeatherApiResponse.builder()
                                .success(false)
                                .errorMessage(error)
                                .build())
                .orElseGet(() ->
                        WeatherApiResponse.builder()
                                .temperature(container.weatherMetrics().temp())
                                .city(container.cityName())
                                .country(container.sunActivityInfo().country())
                                .weatherDescription(container.weather().get(0).description())
                                .build());

    }

    private void validateZipCode(Integer zipcode){
        if (zipcode == null) {
            throw new InvalidZipCodeException(EMPTY_ZIPCODE_EXCEPTION_MESSAGE);
        }
        if (String.valueOf(zipcode).length() != 5) {
            throw new InvalidZipCodeException(INVALID_ZIPCODE_EXCEPTION_MESSAGE);
        }
    }

}
