package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.config.GeneralSettings;
import com.example.weatherwaveapi.config.OpenWeatherApi;
import com.example.weatherwaveapi.config.YandexApi;
import com.example.weatherwaveapi.model.request.CoordinateWeatherRequest;
import com.example.weatherwaveapi.model.request.YandexWeatherRequest;
import com.example.weatherwaveapi.model.response.weatherapi.WeatherOpenApiContainer;
import com.example.weatherwaveapi.model.response.weatherapi.forecast.City;
import com.example.weatherwaveapi.model.response.weatherapi.forecast.ForecastDataContainer;
import com.example.weatherwaveapi.model.response.weatherapi.forecast.WeatherForecastResponse;
import com.example.weatherwaveapi.model.response.weatherapi.weather.Weather;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherMetrics;
import com.example.weatherwaveapi.model.response.yandexapi.YandexApiContainer;
import com.example.weatherwaveapi.model.response.yandexapi.forecast.Forecast;
import com.example.weatherwaveapi.model.response.yandexapi.forecast.ForecastPart;
import com.example.weatherwaveapi.model.response.yandexapi.forecast.YandexForecastResponse;
import com.example.weatherwaveapi.model.response.yandexapi.weather.CurrentWeatherInfo;
import com.example.weatherwaveapi.model.response.yandexapi.weather.LocalityInfo;
import com.example.weatherwaveapi.model.response.yandexapi.weather.YandexWeatherApiResponse;
import com.example.weatherwaveapi.model.response.yandexapi.weather.YandexWeatherResponse;
import com.example.weatherwaveapi.util.urlbuilder.OpenWeatherUrlBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.assertj.core.api.InstanceOfAssertFactories;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.example.util.WeatherApiUtil.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class YandexWeatherServiceTest {
    private MockWebServer mockWebServer;
    private GeneralSettings generalSettings;
    private OpenWeatherUrlBuilder urlBuilder;
    private YandexWeatherService yandexService;
    private static final String LOCALHOST = "http://localhost:";
    private static final String SKIP = "";
    private static final String PLACEHOLDER = "%s";

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        generalSettings = new GeneralSettings();
        urlBuilder = new OpenWeatherUrlBuilder(generalSettings);
        YandexApi yandexApi= new YandexApi(SKIP, LOCALHOST + mockWebServer.getPort(), SKIP);
        generalSettings.setYandexApi(yandexApi);
        yandexService = new YandexWeatherService(new RestTemplate(), generalSettings, urlBuilder);
        String url = String.format(LOCALHOST+PLACEHOLDER, mockWebServer.getPort());
        mockWebServer.url(url + "/weather");
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-type", "application/json")
                        .setBody(createBody()));
    }

    @Test
    void testGetWeather(){
        RestTemplate restTemplate = mock(RestTemplate.class);
        YandexWeatherService yandexService = new YandexWeatherService(restTemplate, generalSettings, urlBuilder);

        YandexApiContainer mockContainerForWeather = YandexApiContainer.builder()
                .info(LocalityInfo.builder()
                        .lon(MOSCOW_LON)
                        .lat(MOSCOW_LAT)
                        .build())
                .serverTimeUTC(DATE)
                .weatherInfo(CurrentWeatherInfo.builder()
                        .temp(MOSCOW_TEMPERATURE)
                        .condition(DESCRIPTION)
                        .build())
                .build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mockContainerForWeather));
        YandexWeatherRequest request = new YandexWeatherRequest(List.of(new CoordinateWeatherRequest(MOSCOW_LAT, MOSCOW_LON)));
        YandexWeatherResponse actualResponse = yandexService.getWeather(request);

        assertThat(actualResponse.responses())
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
    void testGetWeatherErrorMessage(){
        RestTemplate restTemplate = mock(RestTemplate.class);
        YandexWeatherService yandexService = new YandexWeatherService(restTemplate, generalSettings, urlBuilder);
        YandexWeatherRequest request = new YandexWeatherRequest(List.of(new CoordinateWeatherRequest(MOSCOW_LAT, MOSCOW_LON)));
        YandexApiContainer mockContainerForWeather = YandexApiContainer.builder()
                .errorMessage(String.format(INVALID_COORDINATES_MESSAGE_FORMAT, request, WEATHER_ERROR_MESSAGE))
                .build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mockContainerForWeather));
        YandexWeatherResponse actualResponse = yandexService.getWeather(request);

        assertThat(actualResponse.responses())
                .isNotNull()
                .asInstanceOf(InstanceOfAssertFactories.list(YandexWeatherApiResponse.class))
                .hasSize(1)
                .first()
                .returns(false, YandexWeatherApiResponse::success)
                .returns(mockContainerForWeather.errorMessage(), YandexWeatherApiResponse::errorMessage);

    }
    @Test
    void testGetForecast() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        YandexWeatherService yandexService = new YandexWeatherService(restTemplate, generalSettings, urlBuilder);


        YandexApiContainer mockContainerForForecast = YandexApiContainer.builder()
                .info(LocalityInfo.builder()
                        .lon(MOSCOW_LON)
                        .lat(MOSCOW_LAT)
                        .build())
                .forecast(Forecast.builder()
                        .date(DATE)
                        .week(WEEK_NUMBER)
                        .parts(List.of(ForecastPart.builder()
                                        .daytime(DAY_TIME)
                                        .tempAvg(MOSCOW_TEMPERATURE)
                                        .condition(DESCRIPTION)
                                .build()))
                        .build())
                .build();

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



    private YandexApiContainer createMockContainer() {
        return YandexApiContainer.builder()
                .info(LocalityInfo.builder()
                        .lat(MOSCOW_LAT)
                        .lon(MOSCOW_LON)
                        .build())
                .build();
    }
    private String createBody() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(createMockContainer());
    }

}
