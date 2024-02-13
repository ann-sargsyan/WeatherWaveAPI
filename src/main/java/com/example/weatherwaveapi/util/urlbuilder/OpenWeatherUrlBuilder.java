package com.example.weatherwaveapi.util.urlbuilder;

import com.example.weatherwaveapi.config.GeneralSettings;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.example.weatherwaveapi.util.urlbuilder.ParameterList.*;


@Component
@RequiredArgsConstructor
public class OpenWeatherUrlBuilder {

    private final GeneralSettings generalSettings;

    public String buildWeatherUrl(String query) {
        return buildUrl(OPEN_WEATHER_API_KEY_PARAM, openWeatherApiUrl(), Map.of(QUERY_PARAM, query));
    }

    public String buildForecastUrlForCity(String city) {
        return buildUrl(OPEN_WEATHER_API_KEY_PARAM, openWeatherForecastApiUrl(), Map.of(QUERY_PARAM, city));
    }

    public String buildOpenWeatherUrlForZipcode(Integer zipcode, String country) {
        return UriBuilder.fromUri(openWeatherApiUrl())
                .queryParam(OPEN_WEATHER_API_KEY_PARAM, openWeatherApiKey())
                .queryParam(ZIP_PARAM, zipcode, country)
                .build()
                .toString();
    }

    public String buildOpenWeatherUrlForCoord(double lat, double lon) {
        return buildUrl(OPEN_WEATHER_API_KEY_PARAM, openWeatherApiUrl(), Map.of(LAT_PARAM, lat, LON_PARAM, lon));
    }

    public String buildYandexWeatherUrlForCoord(double lat, double lon) {
        return buildUrl(yandexWeatherApiUrl(), Map.of(LAT_PARAM, lat, LON_PARAM, lon));
    }

    private String buildUrl(String keyParam, String apiUrl, Map<String, Object> queryParams) {
        UriBuilder uriBuilder = UriBuilder
                .fromUri(apiUrl)
                .queryParam(keyParam, openWeatherApiKey());

        queryParams.forEach(uriBuilder::queryParam);

        return uriBuilder.build().toString();
    }

    private String buildUrl(String apiUrl, Map<String, Object> queryParams) {
        UriBuilder uriBuilder = UriBuilder
                .fromUri(apiUrl);

        queryParams.forEach(uriBuilder::queryParam);

        return uriBuilder.build().toString();
    }

    private String openWeatherApiUrl() {
        return generalSettings.getOpenWeatherApi().weatherUrl();
    }

    private String openWeatherForecastApiUrl() {
        return generalSettings.getOpenWeatherApi().forecastUrl();
    }

    private String openWeatherApiKey() {
        return generalSettings.getOpenWeatherApi().key();
    }

    private String yandexWeatherApiUrl() {
        return generalSettings.getYandexApi().url();
    }

}
