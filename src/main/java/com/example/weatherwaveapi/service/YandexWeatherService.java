package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.config.GeneralSettings;
import com.example.weatherwaveapi.model.request.CoordinateWeatherRequest;
import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.model.response.yandexapi.YandexApiContainer;
import com.example.weatherwaveapi.model.response.yandexapi.forecast.YandexForecastResponse;
import com.example.weatherwaveapi.model.response.yandexapi.weather.YandexWeatherApiResponse;
import com.example.weatherwaveapi.model.response.yandexapi.weather.YandexWeatherResponse;
import com.example.weatherwaveapi.util.urlbuilder.UrlBuilderForWeather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.GET;

@RequiredArgsConstructor
@Service
@Slf4j
public class YandexWeatherService implements WeatherService {
    private static final String EXCEPTION_MESSAGE = "HTTP error occurred while processing request. Exception message: {}";
    private static final String STRING_MESSAGE_FORMAT = "Invalid coordinates: %s. %s";
    private static final String WEATHER_ERROR_MESSAGE = "Failed to retrieve weather data";
    private static final String FORECAST_ERROR_MESSAGE = "Failed to retrieve forecast data";

    private final RestTemplate restTemplate;
    private final GeneralSettings settings;
    private final UrlBuilderForWeather urlBuilderForWeather;


    public WeatherResponse getWeather(WeatherRequest request) {
        List<YandexWeatherApiResponse> weatherResponses = convertCoordinatesRequest(request.coordinates())
                .stream()
                .map(c -> getYandexApiContainer(urlBuilderForWeather.buildYandexWeatherUrlForCoord(c.lat(), c.lon()), String.format(STRING_MESSAGE_FORMAT, c, WEATHER_ERROR_MESSAGE)))
                .map(YandexApiContainer::convertContainerForWeather)
                .toList();

        YandexWeatherResponse yandexWeatherResponse = YandexWeatherResponse.builder()
                .responses(weatherResponses)
                .build();
        return WeatherResponse.builder().yandexWeatherResponse(yandexWeatherResponse).build();

    }

    public YandexForecastResponse getForecast(CoordinateWeatherRequest request) {
        String url = urlBuilderForWeather.buildYandexWeatherUrlForCoord(request.lat(), request.lon());
        return getYandexApiContainer(url, String.format(STRING_MESSAGE_FORMAT, request, FORECAST_ERROR_MESSAGE))
                .convertContainerForForecast();
    }

    private YandexApiContainer getYandexApiContainer(String url, String format) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(settings.getYandexApi().key(), settings.getYandexApi().value());

            ResponseEntity<YandexApiContainer> responseEntity = restTemplate.exchange(
                    url,
                    GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {
                    }
            );
            return responseEntity.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(EXCEPTION_MESSAGE, e.getMessage());
            return YandexApiContainer.builder()
                    .errorMessage(format)
                    .build();
        }
    }

    private List<CoordinateWeatherRequest> convertCoordinatesRequest(List<String> coordinates) {
        return Optional.ofNullable(coordinates)
                .map(this::coordinateBuilder)
                .orElse(new ArrayList<>());
    }

    private List<CoordinateWeatherRequest> coordinateBuilder(List<String> coordinates) {
        return coordinates.stream()
                .map(coordinate -> coordinate.split(":"))
                .filter(splitCoordinate -> splitCoordinate.length == 2)
                .map(splitCoordinate ->
                        CoordinateWeatherRequest.builder()
                                .lat(Double.parseDouble(splitCoordinate[0]))
                                .lon(Double.parseDouble(splitCoordinate[1]))
                                .build()
                )
                .collect(Collectors.toList());
    }
}
