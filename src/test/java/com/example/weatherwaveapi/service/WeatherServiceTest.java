package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.config.GeneralSettings;
import com.example.weatherwaveapi.config.OpenWeatherApi;
import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherApiResponse;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.model.response.weatherapi.WeatherOpenApiContainer;
import com.example.weatherwaveapi.model.response.weatherapi.weather.SunActivityInfo;
import com.example.weatherwaveapi.model.response.weatherapi.weather.Weather;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherMetrics;
import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.example.util.WeatherApiUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {
    private static final String EXCEPTION_MESSAGE = "The 'getWeatherBySelectedCity' method returned a null container for city: ";
    private static final String LOCALHOST = "http://localhost:";
    private static final String EXPECTED_PATH_LONDON = "/?q=London&appid=";
    private static final String PLACEHOLDER = "%s";
    private static final String SKIP = "";
    private static final String GET = "GET";
    private WeatherService weatherService;
    private MockWebServer mockWebServer;
    private GeneralSettings generalSettings;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        generalSettings = new GeneralSettings();
        OpenWeatherApi openWeatherApi = new OpenWeatherApi(SKIP, LOCALHOST + mockWebServer.getPort(), SKIP);
        generalSettings.setOpenWeatherApi(openWeatherApi);
        weatherService = new WeatherService(new RestTemplate(), generalSettings);
        String url = String.format(LOCALHOST + PLACEHOLDER, mockWebServer.getPort());
        mockWebServer.url(url + "/weather");
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-type", "application/json")
                        .setBody(createBody()));
    }

    @Test
    void testGetWeatherBySelectedCity() throws InterruptedException {
        String city = LONDON;
        WeatherOpenApiContainer container;
        try {
            container = weatherService.getWeatherBySelectedCity(city);
        } catch (NullPointerException e) {
            throw new IllegalStateException(EXCEPTION_MESSAGE + city, e);
        }
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals(request.getMethod(), GET);
        assertEquals(EXPECTED_PATH_LONDON, request.getPath());
        assertEquals(container.cityName(), LONDON);
    }

    @Test
    void testGetWeather() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        WeatherService weatherService = new WeatherService(restTemplate, generalSettings);
        List<String> cities = Arrays.asList(YEREVAN);
        WeatherRequest weatherRequest = new WeatherRequest(cities, ServiceApiEnum.OPEN_WEATHER_MAP);
        WeatherOpenApiContainer weatherOpenApiContainer = WeatherOpenApiContainer
                .builder()
                .weatherMetrics(WeatherMetrics.builder()
                        .temp(YEREVAN_TEMPERATURE)
                        .build())
                .cityName(YEREVAN)
                .sunActivityInfo(SunActivityInfo
                        .builder()
                        .country(COUNTRY_OF_YEREVAN)
                        .build())
                .weather(List.of(Weather.builder()
                        .description(YEREVAN_CLOUDS)
                        .build()))
                .build();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(weatherOpenApiContainer));

        WeatherResponse actualResponse = weatherService.getWeather(weatherRequest);


        assertThat(actualResponse.responses())
                .isNotNull()
                .asInstanceOf(InstanceOfAssertFactories.list(WeatherApiResponse.class))
                .hasSize(1)
                .first()
                .returns(YEREVAN_TEMPERATURE, WeatherApiResponse::temperature)
                .returns(YEREVAN, WeatherApiResponse::city)
                .returns(COUNTRY_OF_YEREVAN, WeatherApiResponse::country)
                .returns(YEREVAN_CLOUDS, WeatherApiResponse::weatherDescription);
    }

    @Test
    void testGetWeather_Failure() {
        WeatherService mockedWeatherService = Mockito.mock(WeatherService.class);
        List<String> cities = Arrays.asList(LONDON, YEREVAN);
        WeatherRequest request = new WeatherRequest(cities, ServiceApiEnum.OPEN_WEATHER_MAP);

        when(mockedWeatherService.getWeather(any(WeatherRequest.class))).thenThrow(new RuntimeException("Test exception"));

        assertThrows(RuntimeException.class, () -> mockedWeatherService.getWeather(request));
    }

    private WeatherOpenApiContainer createMockContainer() {
        return WeatherOpenApiContainer.builder()
                .cityName(LONDON)
                .build();
    }

    private String createBody() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(createMockContainer());
    }
}