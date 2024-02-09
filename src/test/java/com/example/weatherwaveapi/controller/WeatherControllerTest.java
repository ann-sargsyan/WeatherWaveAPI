package com.example.weatherwaveapi.controller;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherApiResponse;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.service.WeatherService;
import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.example.weatherwaveapi.util.WeatherApiUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

    @ParameterizedTest
    @MethodSource("parameters")
    void testGetWeatherInSelectedCity_Success(String url, WeatherApiResponse weatherApiResponse) throws Exception {
        WeatherResponse mockedResponse = WeatherResponse.builder().responses(List.of(weatherApiResponse)).build();

        when(weatherService.getWeather(any(WeatherRequest.class))).thenReturn(mockedResponse);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.responses[0].city").value(weatherApiResponse.city()),
                        jsonPath("$.responses[0].country").value(weatherApiResponse.country()),
                        jsonPath("$.responses[0].temperature").value(weatherApiResponse.temperature()),
                        jsonPath("$.responses[0].weatherDescription").value(weatherApiResponse.weatherDescription()));
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
    void testGetWeatherInSelectedCities_Success(String url, WeatherApiResponse weatherApiResponseLondon, WeatherApiResponse weatherApiResponseYerevan) throws Exception {
        WeatherResponse mockedResponse = WeatherResponse.builder().responses(List.of(weatherApiResponseLondon, weatherApiResponseYerevan)).build();

        when(weatherService.getWeather(any(WeatherRequest.class))).thenReturn(mockedResponse);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.responses[0].city").value(weatherApiResponseLondon.city()),
                        jsonPath("$.responses[0].country").value(weatherApiResponseLondon.country()),
                        jsonPath("$.responses[0].temperature").value(weatherApiResponseLondon.temperature()),
                        jsonPath("$.responses[0].weatherDescription").value(weatherApiResponseLondon.weatherDescription()),
                        jsonPath("$.responses[1].city").value(weatherApiResponseYerevan.city()),
                        jsonPath("$.responses[1].country").value(weatherApiResponseYerevan.country()),
                        jsonPath("$.responses[1].temperature").value(weatherApiResponseYerevan.temperature()),
                        jsonPath("$.responses[1].weatherDescription").value(weatherApiResponseYerevan.weatherDescription()));
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

    @Test
    public void testGetWeatherInSelectedCities_ServiceApi() {
        WeatherService weatherServiceMock = mock(WeatherService.class);
        WeatherController weatherController = new WeatherController(weatherServiceMock);

        List<String> testCities = Arrays.asList(YEREVAN, LONDON);
        ServiceApiEnum testServiceApi = ServiceApiEnum.OPEN_WEATHER_MAP;

        ResponseEntity<WeatherResponse> responseEntity = weatherController.getWeatherInSelectedCities(testCities, testServiceApi);

        verify(weatherServiceMock).getWeather(argThat(weatherRequest ->
                weatherRequest.cities().equals(testCities) && weatherRequest.service() == testServiceApi));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

}