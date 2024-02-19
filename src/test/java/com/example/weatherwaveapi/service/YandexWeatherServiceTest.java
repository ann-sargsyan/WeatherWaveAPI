package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.config.GeneralSettings;
import com.example.weatherwaveapi.config.YandexApi;
import com.example.weatherwaveapi.model.request.CoordinateWeatherRequest;
import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.request.YandexWeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.model.response.yandexapi.YandexApiContainer;
import com.example.weatherwaveapi.model.response.yandexapi.forecast.YandexForecastResponse;
import com.example.weatherwaveapi.model.response.yandexapi.weather.YandexWeatherApiResponse;
import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import com.example.weatherwaveapi.util.urlbuilder.UrlBuilderForWeather;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.example.util.WeatherApiUtil.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class YandexWeatherServiceTest {
    private UrlBuilderForWeather urlBuilder;
    private YandexApi yandexApi;
    private YandexWeatherService yandexService;
    @Mock
    private MockWebServer mockWebServer;
    @Mock
    private GeneralSettings generalSettings;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        urlBuilder = new UrlBuilderForWeather(generalSettings);
        yandexApi = new YandexApi(SKIP, LOCALHOST + mockWebServer.getPort(), SKIP);
        yandexService = new YandexWeatherService(restTemplate, generalSettings, urlBuilder);
    }

    @Test
    void testGetWeather() {
        WeatherRequest weatherRequest = WeatherRequest.builder().coordinates(List.of(new CoordinateWeatherRequest(MOSCOW_LAT, MOSCOW_LON))).build();
        YandexApiContainer mockContainerForWeather = getContainerForYandexWeather();

        when(generalSettings.getYandexApi()).thenReturn(yandexApi);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mockContainerForWeather));

        WeatherResponse actualResponse = yandexService.getWeather(weatherRequest);

        assertThat(actualResponse.yandexWeatherResponse().responses())
                .isNotNull()
                .asInstanceOf(InstanceOfAssertFactories.list(YandexWeatherApiResponse.class))
                .hasSize(1)
                .first()
                .returns(MOSCOW_LON, YandexWeatherApiResponse::longitude)
                .returns(MOSCOW_LAT, YandexWeatherApiResponse::latitude)
                .returns(DATE, YandexWeatherApiResponse::date)
                .returns(MOSCOW_TEMPERATURE, YandexWeatherApiResponse::temperature)
                .returns(DESCRIPTION, YandexWeatherApiResponse::condition);

    }

    @Test
    void testGetWeatherErrorMessage() {
        YandexWeatherRequest yandexWeatherRequest = new YandexWeatherRequest(List.of(new CoordinateWeatherRequest(MOSCOW_LAT, MOSCOW_LON)), ServiceApiEnum.YANDEX);
        WeatherRequest weatherRequest = WeatherRequest.builder().coordinates(List.of(new CoordinateWeatherRequest(MOSCOW_LAT, MOSCOW_LON))).build();
        YandexApiContainer mockContainerForWeather = getErrorContainerForYandexWeather(yandexWeatherRequest);

        when(generalSettings.getYandexApi()).thenReturn(yandexApi);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mockContainerForWeather));


        WeatherResponse actualResponse = yandexService.getWeather(weatherRequest);

        assertThat(actualResponse.yandexWeatherResponse().responses())
                .isNotNull()
                .asInstanceOf(InstanceOfAssertFactories.list(YandexWeatherApiResponse.class))
                .hasSize(1)
                .first()
                .returns(mockContainerForWeather.errorMessage(), YandexWeatherApiResponse::errorMessage);

    }

    @Test
    void testGetForecast() {
        YandexApiContainer mockContainerForForecast = getContainerForYandexForecast();

        when(generalSettings.getYandexApi()).thenReturn(yandexApi);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mockContainerForForecast));

        YandexForecastResponse forecastResponse = yandexService.getForecast(new CoordinateWeatherRequest(MOSCOW_LAT, MOSCOW_LON));

        assertAll(
                () -> assertEquals(mockContainerForForecast.info().lat(), forecastResponse.latitude()),
                () -> assertEquals(mockContainerForForecast.info().lon(), forecastResponse.longitude()),
                () -> assertEquals(mockContainerForForecast.forecast().date(), forecastResponse.date()),
                () -> assertEquals(mockContainerForForecast.forecast().week(), forecastResponse.week()),
                () -> assertEquals(mockContainerForForecast.forecast().parts().get(0).condition(), forecastResponse.forecast().get(0).condition()),
                () -> assertEquals(mockContainerForForecast.forecast().parts().get(0).tempAvg(), forecastResponse.forecast().get(0).temperature())
        );
    }


}