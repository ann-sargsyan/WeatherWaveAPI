package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherApiResponse;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.model.response.weatherapi.WeatherOpenApiContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@RequiredArgsConstructor
@Service
@Slf4j
public class WeatherService {
    private static final String EXCEPTION_MESSAGE = "HTTP error occurred while processing request. Exception message: {}";

    @Value("${openWeatherMap.api.key}")
    private String apiKey;

    @Value("${openWeatherMap.api.url}")
    private String apiUrl;
    public RestTemplate restTemplate = new RestTemplate();

    public WeatherOpenApiContainer getWeatherBySelectedCity(String city) {
        String url = getUrlForSelectedCity(city);
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
            throw new RuntimeException(e);
        }
    }

    public WeatherResponse getWeather(WeatherRequest request) {

        List<WeatherApiResponse> weatherResponses = new ArrayList<>();
        for (String city : request.cities()) {
            WeatherOpenApiContainer weatherApiResponse = getWeatherBySelectedCity(city);
            weatherResponses.add(convertContainer(weatherApiResponse));
        }

        return WeatherResponse.builder()
                .responses(weatherResponses)
                .build();
    }

    private String getUrlForSelectedCity(String city) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", city)
                .queryParam("appid", apiKey);

        return builder.toUriString();
    }

    private WeatherApiResponse convertContainer(WeatherOpenApiContainer container) {
        return WeatherApiResponse.builder()
                .success(true)
                .temperature(container.weatherMetrics().temp())
                .city(container.name())
                .country(container.sunActivityInfo().country())
                .weatherDescription(container.weather().get(0).description())
                .build();

    }
}
