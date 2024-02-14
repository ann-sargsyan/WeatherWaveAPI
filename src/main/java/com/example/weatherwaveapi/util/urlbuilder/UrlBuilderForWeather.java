package com.example.weatherwaveapi.util.urlbuilder;

import com.example.weatherwaveapi.config.GeneralSettings;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.example.weatherwaveapi.util.urlbuilder.ParameterList.*;


@Component
@RequiredArgsConstructor
public class UrlBuilderForWeather {

    private final GeneralSettings generalSettings;

    public String buildWeatherUrl(String query) {
        return buildUrl(
                OPEN_WEATHER_API_KEY_PARAM,
                generalSettings.getOpenWeatherApi().key(),
                generalSettings.getOpenWeatherApi().weatherUrl(),
                Map.of(QUERY_PARAM, query)
        );
    }

    public String buildForecastUrlForCity(String city) {
        return buildUrl(
                OPEN_WEATHER_API_KEY_PARAM,
                generalSettings.getOpenWeatherApi().key(),
                generalSettings.getOpenWeatherApi().forecastUrl(),
                Map.of(QUERY_PARAM, city)
        );
    }

    public String buildOpenWeatherUrlForZipcode(Integer zipcode, String country) {
        return UriBuilder.fromUri(generalSettings.getOpenWeatherApi().weatherUrl())
                .queryParam(OPEN_WEATHER_API_KEY_PARAM, generalSettings.getOpenWeatherApi().key())
                .queryParam(ZIP_PARAM, zipcode, country)
                .build()
                .toString();
    }

    public String buildOpenWeatherUrlForCoord(double lat, double lon) {
        return buildUrl(
                OPEN_WEATHER_API_KEY_PARAM,
                generalSettings.getOpenWeatherApi().key(),
                generalSettings.getOpenWeatherApi().weatherUrl(),
                Map.of(LAT_PARAM, lat, LON_PARAM, lon)
        );
    }

    public String buildYandexWeatherUrlForCoord(double lat, double lon) {
        return buildUrl(
                generalSettings.getYandexApi().url(),
                Map.of(LAT_PARAM, lat, LON_PARAM, lon)
        );
    }

    private String buildUrl(String keyParam, String key, String apiUrl, Map<String, Object> queryParams) {
        UriBuilder uriBuilder = UriBuilder
                .fromUri(apiUrl)
                .queryParam(keyParam, key);

        queryParams.forEach(uriBuilder::queryParam);

        return uriBuilder.build().toString();
    }

    private String buildUrl(String apiUrl, Map<String, Object> queryParams) {
        UriBuilder uriBuilder = UriBuilder
                .fromUri(apiUrl);

        queryParams.forEach(uriBuilder::queryParam);

        return uriBuilder.build().toString();
    }
}
