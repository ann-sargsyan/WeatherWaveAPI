package com.example.weatherwaveapi.controller;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherApiResponse;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static com.example.util.WeatherApiUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WeatherController.class)
class WeatherControllerTest {
    private static final String URL_FOR_CITY_LONDON = "http://localhost:8080/weather/city?city=london";
    private static final String URL_FOR_CITIES = "http://localhost:8080/weather/cities?cities=yerevan&cities=london";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void testGetWeatherInSelectedCity_Success() throws Exception {
        WeatherApiResponse weatherApiResponse = new WeatherApiResponse(true, LONDON, COUNTRY_OF_LONDON, LONDON_TEMPERATURE, LONDON_CLOUDS);
        WeatherResponse mockedResponse = WeatherResponse.builder().responses(List.of(weatherApiResponse)).build();

        when(weatherService.getWeather(any(WeatherRequest.class))).thenReturn(mockedResponse);

        mockMvc.perform(get(URL_FOR_CITY_LONDON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.responses[0].success").value(true),
                        jsonPath("$.responses[0].city").value(LONDON),
                        jsonPath("$.responses[0].country").value(COUNTRY_OF_LONDON),
                        jsonPath("$.responses[0].temperature").value(LONDON_TEMPERATURE),
                        jsonPath("$.responses[0].weatherDescription").value(LONDON_CLOUDS));
    }

    @Test
    void testGetWeatherInSelectedCities_Success() throws Exception {
        WeatherApiResponse weatherApiResponseLondon = new WeatherApiResponse(true, LONDON, COUNTRY_OF_LONDON, LONDON_TEMPERATURE, LONDON_CLOUDS);
        WeatherApiResponse weatherApiResponseYerevan = new WeatherApiResponse(true, YEREVAN, COUNTRY_OF_YEREVAN, YEREVAN_TEMPERATURE, YEREVAN_CLOUDS);
        WeatherResponse mockedResponse = WeatherResponse.builder().responses(List.of(weatherApiResponseLondon, weatherApiResponseYerevan)).build();

        when(weatherService.getWeather(any(WeatherRequest.class))).thenReturn(mockedResponse);

        mockMvc.perform(get(URL_FOR_CITIES))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.responses[0].success").value(true),
                        jsonPath("$.responses[0].city").value(LONDON),
                        jsonPath("$.responses[0].country").value(COUNTRY_OF_LONDON),
                        jsonPath("$.responses[0].temperature").value(LONDON_TEMPERATURE),
                        jsonPath("$.responses[0].weatherDescription").value(LONDON_CLOUDS),
                        jsonPath("$.responses[1].success").value(true),
                        jsonPath("$.responses[1].city").value(YEREVAN),
                        jsonPath("$.responses[1].country").value(COUNTRY_OF_YEREVAN),
                        jsonPath("$.responses[1].temperature").value(YEREVAN_TEMPERATURE),
                        jsonPath("$.responses[1].weatherDescription").value(YEREVAN_CLOUDS));
    }

    @Test
    void testGetWeatherInSelectedCity_Failure() throws Exception {
        WeatherApiResponse failedApiResponse = new WeatherApiResponse(false, null, null, 0.0, null);
        WeatherResponse failedResponse = WeatherResponse.builder().responses(List.of(failedApiResponse)).build();

        when(weatherService.getWeather(any(WeatherRequest.class))).thenReturn(failedResponse);

        mockMvc.perform(get(URL_FOR_CITY_LONDON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responses[0].success").value(false));
    }

    @Test
    void testGetWeatherInSelectedCities_Failure() throws Exception {
        WeatherApiResponse failedApiResponse = new WeatherApiResponse(false, null, null, 0.0, null);
        WeatherResponse failedResponse = WeatherResponse.builder().responses(List.of(failedApiResponse)).build();

        when(weatherService.getWeather(any(WeatherRequest.class))).thenReturn(failedResponse);

        mockMvc.perform(get(URL_FOR_CITIES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responses[0].success").value(false));
    }
}
