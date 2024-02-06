package com.example.service;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherApiResponse;
import com.example.weatherwaveapi.model.response.WeatherForecastResponse;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.service.WeatherService;
import com.example.weatherwaveapi.util.urlbuilder.OpenWeatherUrlBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.HttpMethod.GET;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static com.example.TestUtil.*;
@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OpenWeatherUrlBuilder urlBuilder;

    @InjectMocks
    private WeatherService weatherService;


    @ParameterizedTest
    @MethodSource("parameters")
    void testGetWeather(WeatherRequest weatherRequest) {
        when(urlBuilder.buildWeatherUrl(any())).thenReturn(URL);
        when(restTemplate.exchange(eq(URL), eq(GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mockContainerForWeather));

        WeatherResponse weatherResponse = weatherService.getWeather(weatherRequest);
        WeatherApiResponse response = weatherResponse.responses().get(0);

        assertAll(
                () -> assertEquals(1, weatherResponse.responses().size()),
                () -> assertNotNull(response),
                () -> assertTrue(response.success()),
                () -> assertEquals(mockContainerForWeather.cityName(), response.city()),
                () -> assertEquals(mockContainerForWeather.sunActivityInfo().country(), response.country()),
                () -> assertEquals(mockContainerForWeather.weatherMetrics().temp(), response.temperature()),
                () -> assertEquals(mockContainerForWeather.visibility().toString(), response.weatherDescription())
        );
    }

    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(WeatherRequest.builder()
                        .cities(List.of(CITY))
                        .build()),
                Arguments.of(WeatherRequest.builder()
                        .zipCodeCountryMap(Collections.singletonMap(ZIPCODE, COUNTRY))
                        .build())
        );
    }

    @Test
    void testGetForecast() {
        when(urlBuilder.buildForecastUrlForCity(eq(CITY))).thenReturn(URL);
        when(restTemplate.exchange(eq(URL), eq(GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mockContainerForForecast));

        WeatherForecastResponse forecastResponse = weatherService.getForecast(CITY);

        assertAll(
                () -> assertTrue(forecastResponse.success()),
                () -> assertEquals(mockContainerForForecast.list(), forecastResponse.weather()),
                () -> assertEquals(mockContainerForForecast.cityDetails().name(), forecastResponse.city()),
                () -> assertEquals(mockContainerForForecast.cityDetails().country(), forecastResponse.country())
        );
    }

}
