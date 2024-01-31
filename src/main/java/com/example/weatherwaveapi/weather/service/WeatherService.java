package com.example.weatherwaveapi.weather.service;

import com.example.weatherwaveapi.model.response.weatherforecastresponse.WeatherForecastResponse;
import com.example.weatherwaveapi.model.response.weatherresponse.WeatherResponse;
import com.example.weatherwaveapi.util.urlbuilder.OpenWeatherUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


import static org.springframework.http.HttpMethod.GET;

@RequiredArgsConstructor
@Service
@Slf4j
public class WeatherService {
    private static final String EXCEPTION_MESSAGE = "HTTP error occurred while processing request. Exception message: {}";
    private OpenWeatherUrlBuilder urlBuilder;
    public RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public WeatherService(OpenWeatherUrlBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    public WeatherResponse getWeatherBySelectedCity(String city) {
        String url = urlBuilder.buildWeatherUrl(city);
        return getResponse(url);
    }

    public WeatherResponse getWeatherByZipCode(Integer zipcode, String country) {
        String url;
        if (country == null) {
            url = urlBuilder.buildWeatherUrl(zipcode.toString());
        } else {
            url = urlBuilder.buildWeatherUrlForZipcode(zipcode, country);
        }

        return getResponse(url);
    }

    public WeatherForecastResponse getForecastByCity(String city) {
        String url = urlBuilder.buildForecastUrlForCity(city);
        return getResponse(url);
    }

    private <T> T getResponse(String url) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(
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
}
