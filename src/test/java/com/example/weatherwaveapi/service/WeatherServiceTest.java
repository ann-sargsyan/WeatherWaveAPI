package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.config.GeneralSettings;
import com.example.weatherwaveapi.config.OpenWeatherApi;
import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.request.ZipCodeWeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherApiResponse;
import com.example.weatherwaveapi.model.response.WeatherForecastResponse;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.model.response.weatherapi.WeatherOpenApiContainer;
import com.example.weatherwaveapi.model.response.weatherapi.forecast.City;
import com.example.weatherwaveapi.model.response.weatherapi.forecast.ForecastDataContainer;
import com.example.weatherwaveapi.model.response.weatherapi.weather.SunActivityInfo;
import com.example.weatherwaveapi.model.response.weatherapi.weather.Weather;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherMetrics;
import com.example.weatherwaveapi.util.urlbuilder.OpenWeatherUrlBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

import static com.example.util.WeatherApiUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {
    private static final String EXCEPTION_MESSAGE = "The 'getWeatherBySelectedCity' method returned a null container for city: ";
    private static final String LOCALHOST = "http://localhost:";
    private static final String EXPECTED_PATH_LONDON = "/?appid=&q=London";
    private static final String PLACEHOLDER = "%s";
    private static final String SKIP = "";
    private static final String GET = "GET";
    private WeatherService weatherService;
    private MockWebServer mockWebServer;
    private GeneralSettings generalSettings;
    private OpenWeatherUrlBuilder urlBuilder;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        generalSettings = new GeneralSettings();
        urlBuilder = new OpenWeatherUrlBuilder(generalSettings);
        OpenWeatherApi openWeatherApi = new OpenWeatherApi(SKIP, LOCALHOST + mockWebServer.getPort(), SKIP);
        generalSettings.setOpenWeatherApi(openWeatherApi);
        weatherService = new WeatherService(new RestTemplate(), urlBuilder);
        String url = String.format(LOCALHOST + PLACEHOLDER, mockWebServer.getPort());
        mockWebServer.url(url + "/weather");
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-type", "application/json")
                        .setBody(createBody()));
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void testGetWeatherByCities(WeatherRequest weatherRequest) {
        RestTemplate restTemplate = mock(RestTemplate.class);
        WeatherService weatherService = new WeatherService(restTemplate, urlBuilder);

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

    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(WeatherRequest.builder()
                        .cities(List.of(LONDON))
                        .build()),
                Arguments.of(WeatherRequest.builder()
                        .zipcode(List.of(ZipCodeWeatherRequest.builder()
                                .zipcode(ZIPCODE)
                                .country(COUNTRY_USA)
                                .build()))
                        .build())
        );
    }

    @Test
    void testGetWeather_Failure() {
        WeatherService mockedWeatherService = Mockito.mock(WeatherService.class);
        List<String> cities = Arrays.asList(LONDON, YEREVAN);
        WeatherRequest request = WeatherRequest.builder()
                .cities(cities)
                .build();

        when(mockedWeatherService.getWeather(any(WeatherRequest.class))).thenThrow(new RuntimeException("Test exception"));

        assertThrows(RuntimeException.class, () -> mockedWeatherService.getWeather(request));
    }

    @Test
    void testGetForecast() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        WeatherService weatherService = new WeatherService(restTemplate, urlBuilder);

        WeatherOpenApiContainer mockContainerForForecast = WeatherOpenApiContainer.builder()
                .cityDetails(City.builder()
                        .name(LONDON)
                        .country(COUNTRY_OF_LONDON).build())
                .forecastData(List.of(ForecastDataContainer.builder()
                        .date(DATE)
                        .weatherMetrics(WeatherMetrics.builder().temp(LONDON_TEMPERATURE).build())
                        .weather(List.of(Weather.builder().description(LONDON_CLOUDS).build()))
                        .build()))
                .build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mockContainerForForecast));


        WeatherForecastResponse forecastResponse = weatherService.getForecast(LONDON);

        assertAll(
                () -> assertEquals(mockContainerForForecast.forecastContainer(), forecastResponse.forecastData()),
                () -> assertEquals(mockContainerForForecast.cityDetails().name(), forecastResponse.city()),
                () -> assertEquals(mockContainerForForecast.cityDetails().country(), forecastResponse.country())
        );
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