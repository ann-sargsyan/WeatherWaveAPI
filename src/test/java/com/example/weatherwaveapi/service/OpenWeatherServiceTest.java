package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.config.GeneralSettings;
import com.example.weatherwaveapi.config.OpenWeatherApi;
import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.model.response.weatherapi.WeatherOpenApiContainer;
import com.example.weatherwaveapi.model.response.weatherapi.forecast.WeatherForecastResponse;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherApiResponse;
import com.example.weatherwaveapi.util.urlbuilder.UrlBuilderForWeather;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.example.util.WeatherApiUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenWeatherServiceTest {
    private OpenWeatherService openWeatherService;
    private UrlBuilderForWeather urlBuilder;
    private OpenWeatherApi openWeatherApi;
    @Mock
    private MockWebServer mockWebServer;
    @Mock
    private GeneralSettings generalSettings;
    @Mock
    private RestTemplate restTemplate;

    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(WeatherRequest.builder()
                        .cities(List.of(LONDON))
                        .build()),
                Arguments.of(WeatherRequest.builder()
                        .zipcode(List.of(ZIPCODE, COUNTRY_USA))
                        .build())
        );
    }

    @BeforeEach
    void setup() {
        urlBuilder = new UrlBuilderForWeather(generalSettings);
        openWeatherApi = new OpenWeatherApi(SKIP, LOCALHOST + mockWebServer.getPort(), SKIP);
        openWeatherService = new OpenWeatherService(restTemplate, urlBuilder);
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void testGetWeatherByCities(WeatherRequest weatherRequest) {
        WeatherOpenApiContainer weatherOpenApiContainer = getContainerForOpenWeather();

        when(generalSettings.getOpenWeatherApi()).thenReturn(openWeatherApi);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(weatherOpenApiContainer));


        WeatherResponse actualResponse = openWeatherService.getWeather(weatherRequest);


        assertThat(actualResponse.openWeatherResponse().responses())
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
        OpenWeatherService mockedOpenWeatherService = Mockito.mock(OpenWeatherService.class);
        List<String> cities = Arrays.asList(LONDON, YEREVAN);
        WeatherRequest request = WeatherRequest.builder()
                .cities(cities)
                .build();

        when(mockedOpenWeatherService.getWeather(any(WeatherRequest.class))).thenThrow(new RuntimeException("Test exception"));

        assertThrows(RuntimeException.class, () -> mockedOpenWeatherService.getWeather(request));
    }

    @Test
    void testGetForecast() {
        WeatherOpenApiContainer mockContainerForForecast = getContainerForOpenWeatherForecast();

        when(generalSettings.getOpenWeatherApi()).thenReturn(openWeatherApi);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mockContainerForForecast));


        WeatherForecastResponse forecastResponse = openWeatherService.getForecast(LONDON);

        assertAll(
                () -> assertEquals(mockContainerForForecast.forecastContainer(), forecastResponse.forecastData()),
                () -> assertEquals(mockContainerForForecast.cityDetails().name(), forecastResponse.city()),
                () -> assertEquals(mockContainerForForecast.cityDetails().country(), forecastResponse.country())
        );
    }
}
