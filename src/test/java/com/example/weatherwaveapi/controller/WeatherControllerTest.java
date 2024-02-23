package com.example.weatherwaveapi.controller;


import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.model.response.weatherapi.weather.OpenWeatherResponse;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherApiResponse;
import com.example.weatherwaveapi.model.response.yandexapi.weather.YandexWeatherApiResponse;
import com.example.weatherwaveapi.model.response.yandexapi.weather.YandexWeatherResponse;
import com.example.weatherwaveapi.service.OpenWeatherService;
import com.example.weatherwaveapi.service.WeatherRouterService;
import com.example.weatherwaveapi.service.YandexWeatherService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static com.example.util.WeatherApiUtil.*;
import static com.example.weatherwaveapi.serviceapienum.ServiceApiEnum.OPEN_WEATHER_MAP;
import static com.example.weatherwaveapi.serviceapienum.ServiceApiEnum.YANDEX;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {
    private static final String URL_FOR_CITY_LONDON = "http://localhost:8080/weather/params?cities=london";
    private static final String URL_FOR_CITY_MOSCOW = "http://localhost:8080/weather/params?coordinates=55.75396,37.620393&serviceApi=YANDEX";
    private static final String URL_FOR_US = "http://localhost:8080/weather/params?serviceApi=OPEN_WEATHER_MAP&zipCodes=94040,US";
    private static final String URL_FOR_CITIES = "http://localhost:8080/weather/params?cities=london&cities=yerevan";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenWeatherService openWeatherService;

    @MockBean
    private YandexWeatherService yandexWeatherService;

    @MockBean
    private WeatherRouterService weatherRouterService;

    @ParameterizedTest
    @MethodSource("parameters")
    void testGetWeatherInSelectedCity_OpenWeather(String url, WeatherApiResponse weatherApiResponse) throws Exception {
        OpenWeatherResponse openWeatherResponse = OpenWeatherResponse.builder().responses(List.of(weatherApiResponse)).build();
        WeatherResponse mockedResponse = WeatherResponse.builder().openWeatherResponse(openWeatherResponse).build();

        when(weatherRouterService.getWeatherService(OPEN_WEATHER_MAP)).thenReturn(openWeatherService);
        when(openWeatherService.getWeather(any(WeatherRequest.class))).thenReturn(mockedResponse);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.openWeatherResponse.responses[0].city").value(weatherApiResponse.getCity()),
                        jsonPath("$.openWeatherResponse.responses[0].country").value(weatherApiResponse.getCountry()),
                        jsonPath("$.openWeatherResponse.responses[0].temperature").value(weatherApiResponse.getTemperature()),
                        jsonPath("$.openWeatherResponse.responses[0].weatherDescription").value(weatherApiResponse.getWeatherDescription()));
    }

    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(URL_FOR_CITY_LONDON, WeatherApiResponse.builder()
                        .city(LONDON)
                        .country(COUNTRY_OF_LONDON)
                        .temperature(LONDON_TEMPERATURE)
                        .weatherDescription(LONDON_CLOUDS)
                        .build())
        );
    }

    @ParameterizedTest
    @MethodSource("cities_parameters")
    void testGetWeatherInSelectedCities_OpenWeather(String url, WeatherApiResponse weatherApiResponseLondon, WeatherApiResponse weatherApiResponseYerevan) throws Exception {
        OpenWeatherResponse openWeatherResponse = OpenWeatherResponse.builder().responses(List.of(weatherApiResponseLondon, weatherApiResponseYerevan)).build();
        WeatherResponse mockedResponse = WeatherResponse.builder().openWeatherResponse(openWeatherResponse).build();

        when(weatherRouterService.getWeatherService(OPEN_WEATHER_MAP)).thenReturn(openWeatherService);
        when(openWeatherService.getWeather(any(WeatherRequest.class))).thenReturn(mockedResponse);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.openWeatherResponse.responses[0].city").value(weatherApiResponseLondon.getCity()),
                        jsonPath("$.openWeatherResponse.responses[0].country").value(weatherApiResponseLondon.getCountry()),
                        jsonPath("$.openWeatherResponse.responses[0].temperature").value(weatherApiResponseLondon.getTemperature()),
                        jsonPath("$.openWeatherResponse.responses[0].weatherDescription").value(weatherApiResponseLondon.getWeatherDescription()),
                        jsonPath("$.openWeatherResponse.responses[1].city").value(weatherApiResponseYerevan.getCity()),
                        jsonPath("$.openWeatherResponse.responses[1].country").value(weatherApiResponseYerevan.getCountry()),
                        jsonPath("$.openWeatherResponse.responses[1].temperature").value(weatherApiResponseYerevan.getTemperature()),
                        jsonPath("$.openWeatherResponse.responses[1].weatherDescription").value(weatherApiResponseYerevan.getWeatherDescription()));
    }

    private static Stream<Arguments> cities_parameters() {
        return Stream.of(
                Arguments.of(URL_FOR_CITIES, WeatherApiResponse.builder()
                        .city(LONDON)
                        .country(COUNTRY_OF_LONDON)
                        .temperature(LONDON_TEMPERATURE)
                        .weatherDescription(LONDON_CLOUDS)
                        .build(), WeatherApiResponse.builder()
                        .city(YEREVAN)
                        .country(COUNTRY_OF_YEREVAN)
                        .temperature(YEREVAN_TEMPERATURE)
                        .weatherDescription(YEREVAN_CLOUDS)
                        .build())
        );
    }

    @ParameterizedTest
    @MethodSource("parameters_for_zipCode")
    void testGetWeatherByZipCode_Yandex(String url, WeatherApiResponse weatherApiResponse) throws Exception {
        OpenWeatherResponse openWeatherResponse = OpenWeatherResponse.builder().responses(List.of(weatherApiResponse)).build();
        WeatherResponse mockedResponse = WeatherResponse.builder().openWeatherResponse(openWeatherResponse).build();

        when(weatherRouterService.getWeatherService(OPEN_WEATHER_MAP)).thenReturn(openWeatherService);
        when(openWeatherService.getWeather(any(WeatherRequest.class))).thenReturn(mockedResponse);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.openWeatherResponse.responses[0].city").value(weatherApiResponse.getCity()),
                        jsonPath("$.openWeatherResponse.responses[0].country").value(weatherApiResponse.getCountry()),
                        jsonPath("$.openWeatherResponse.responses[0].temperature").value(weatherApiResponse.getTemperature()),
                        jsonPath("$.openWeatherResponse.responses[0].weatherDescription").value(weatherApiResponse.getWeatherDescription()));
    }

    private static Stream<Arguments> parameters_for_zipCode() {
        return Stream.of(
                Arguments.of(URL_FOR_US, WeatherApiResponse.builder()
                        .city(CITY_IN_US)
                        .country(COUNTRY_USA)
                        .temperature(US_TEMPERATURE)
                        .weatherDescription(US_DESCRIPTION)
                        .build()));
    }

    @ParameterizedTest
    @MethodSource("parameters_for_yandex")
    void testGetWeatherInSelectedCity_Yandex(String url, YandexWeatherApiResponse yandexWeatherApiResponse) throws Exception {
        YandexWeatherResponse yandexWeatherResponse = YandexWeatherResponse.builder().responses(List.of(yandexWeatherApiResponse)).build();
        WeatherResponse mockedResponse = WeatherResponse.builder().yandexWeatherResponse(yandexWeatherResponse).build();

        when(weatherRouterService.getWeatherService(YANDEX)).thenReturn(yandexWeatherService);
        when(yandexWeatherService.getWeather(any(WeatherRequest.class))).thenReturn(mockedResponse);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.yandexWeatherResponse.responses[0].latitude").value(yandexWeatherApiResponse.getLatitude()),
                        jsonPath("$.yandexWeatherResponse.responses[0].longitude").value(yandexWeatherApiResponse.getLongitude()),
                        jsonPath("$.yandexWeatherResponse.responses[0].temperature").value(yandexWeatherApiResponse.getTemperature()),
                        jsonPath("$.yandexWeatherResponse.responses[0].condition").value(yandexWeatherApiResponse.getCondition()),
                        jsonPath("$.yandexWeatherResponse.responses[0].date").value(yandexWeatherApiResponse.getDate()));
    }

    private static Stream<Arguments> parameters_for_yandex() {
        return Stream.of(
                Arguments.of(URL_FOR_CITY_MOSCOW, YandexWeatherApiResponse.builder()
                        .latitude(MOSCOW_LAT)
                        .longitude(MOSCOW_LON)
                        .temperature(MOSCOW_TEMPERATURE)
                        .condition(DESCRIPTION)
                        .date(DATE)
                        .build()));
    }

}

