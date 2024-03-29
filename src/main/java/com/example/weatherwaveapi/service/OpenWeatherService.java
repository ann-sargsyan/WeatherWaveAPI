package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.model.request.WeatherRequest;
import com.example.weatherwaveapi.model.request.ZipCodeWeatherRequest;
import com.example.weatherwaveapi.model.response.weatherapi.WeatherOpenApiContainer;
import com.example.weatherwaveapi.model.response.weatherapi.forecast.WeatherForecastResponse;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherApiResponse;
import com.example.weatherwaveapi.model.response.weatherapi.weather.OpenWeatherResponse;
import com.example.weatherwaveapi.model.response.WeatherResponse;
import com.example.weatherwaveapi.util.urlbuilder.UrlBuilderForWeather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.GET;

@RequiredArgsConstructor
@Service
@Slf4j
public class OpenWeatherService implements WeatherService {
    private static final String EXCEPTION_MESSAGE = "HTTP error occurred while processing request. Exception message: {}";
    private static final String WEATHER_ERROR_MESSAGE = "Failed to retrieve weather data";
    private static final String FORECAST_ERROR_MESSAGE = "Failed to retrieve forecast data";
    private static final String STRING_MESSAGE_FORMAT = "Search string: %s, error: %s";
    private static final String EMPTY_ZIPCODE_EXCEPTION_MESSAGE = "ZIP code cannot be null";
    private static final String INVALID_ZIPCODE_EXCEPTION_MESSAGE = "ZIP code must be 5 digits long";
    private static final String INVALID_ZIP_CODE_FORMAT_MESSAGE = "Invalid zip code format";

    private final RestTemplate restTemplate;

    private final UrlBuilderForWeather urlBuilderForWeather;

    public WeatherResponse getWeather(WeatherRequest request) {
        List<WeatherApiResponse> weatherResponses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.cities())) {
            weatherResponses = request.cities().stream()
                    .map(this::getWeatherBySelectedCity)
                    .map(this::convertContainer)
                    .collect(Collectors.toList());
        } else {
            List<ZipCodeWeatherRequest> zipCodeWeatherRequests = convertZipCodeRequest(request.zipcode());
            weatherResponses = zipCodeWeatherRequests.stream()
                    .map(x -> getWeatherByZipCode(x.zipcode(), x.country()))
                    .map(this::convertContainer)
                    .collect(Collectors.toList());
        }
        OpenWeatherResponse openWeatherResponse = OpenWeatherResponse.builder()
                .responses(weatherResponses)
                .build();
        return WeatherResponse.builder().openWeatherResponse(openWeatherResponse).build();
    }

    public WeatherForecastResponse getForecast(String city) {
        WeatherOpenApiContainer container = getForecastContainerByCity(city);
        return Optional.ofNullable(container.errorMessage())
                .map(error -> WeatherForecastResponse.builder()
                        .success(false)
                        .errorMessage(error)
                        .build())
                .orElseGet(() -> WeatherForecastResponse.builder()
                        .forecastData(container.forecastContainer())
                        .city(container.cityDetails().name())
                        .country(container.cityDetails().country())
                        .build());
    }

    private WeatherOpenApiContainer getWeatherOpenApiContainer(String url, String format) {
        try {
            ResponseEntity<WeatherOpenApiContainer> responseEntity = restTemplate.exchange(
                    url,
                    GET,
                    new HttpEntity<>(new HttpHeaders()),
                    new ParameterizedTypeReference<>() {
                    }
            );
            return responseEntity.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(EXCEPTION_MESSAGE, e.getMessage());
            return WeatherOpenApiContainer.builder()
                    .errorMessage(format)
                    .build();
        }
    }

    private WeatherOpenApiContainer getWeatherBySelectedCity(String city) {
        String url = urlBuilderForWeather.buildWeatherUrl(city);
        return getWeatherOpenApiContainer(url, String.format(STRING_MESSAGE_FORMAT, city, WEATHER_ERROR_MESSAGE));
    }

    private WeatherOpenApiContainer getWeatherByZipCode(Integer zipcode, String country) {
        validateZipCode(zipcode);
        String url = Optional.ofNullable(country)
                .map(c -> urlBuilderForWeather.buildOpenWeatherUrlForZipcode(zipcode, c))
                .orElse(urlBuilderForWeather.buildWeatherUrl(zipcode.toString()));

        return getWeatherOpenApiContainer(url, String.format(STRING_MESSAGE_FORMAT, zipcode, WEATHER_ERROR_MESSAGE));
    }

    private WeatherOpenApiContainer getForecastContainerByCity(String city) {
        String url = urlBuilderForWeather.buildForecastUrlForCity(city);
        return getWeatherOpenApiContainer(url, String.format(STRING_MESSAGE_FORMAT, city, FORECAST_ERROR_MESSAGE));
    }

    private WeatherApiResponse convertContainer(WeatherOpenApiContainer container) {
        return Optional.ofNullable(container.errorMessage())
                .map(error -> WeatherApiResponse.builder()
                        .success(false)
                        .errorMessage(error)
                        .build())
                .orElseGet(() ->
                        WeatherApiResponse.builder()
                                .temperature(container.weatherMetrics().temp())
                                .city(container.cityName())
                                .country(container.sunActivityInfo().country())
                                .weatherDescription(container.weather().get(0).description())
                                .build());

    }

    private void validateZipCode(Integer zipcode) {
        if (zipcode == null) {
            log.error(EMPTY_ZIPCODE_EXCEPTION_MESSAGE);
        }
        if (String.valueOf(zipcode).length() != 5) {
            log.error(INVALID_ZIPCODE_EXCEPTION_MESSAGE);
        }
    }

    private List<ZipCodeWeatherRequest> convertZipCodeRequest(List<String> zipCodes) {
        if (CollectionUtils.isEmpty(zipCodes)) {
            log.info(INVALID_ZIP_CODE_FORMAT_MESSAGE);
            return Collections.emptyList();
        }
        return zipCodeBuilder(zipCodes);
    }

    private List<ZipCodeWeatherRequest> zipCodeBuilder(List<String> zipCodes) {
        List<ZipCodeWeatherRequest> result = new ArrayList<>();
        try {
            for (int i = 0; i < zipCodes.size(); i += 2) {
                int zipCode = Integer.parseInt(zipCodes.get(i % zipCodes.size()));
                String country = zipCodes.get((i + 1) % zipCodes.size());
                result.add(buildZipCodeRequest(zipCode, country));
            }
        } catch (NumberFormatException e) {
            log.error(INVALID_ZIP_CODE_FORMAT_MESSAGE);
        }
        return result;
    }

    private ZipCodeWeatherRequest buildZipCodeRequest(int zipCode, String country) {
        return ZipCodeWeatherRequest.builder()
                .zipcode(zipCode)
                .country(country)
                .build();
    }

}
