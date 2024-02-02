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
        return buildUrl(weatherApiUrl(), Map.of(QUERY_PARAM, query));
    }

    public String buildForecastUrlForCity(String city) {
        return buildUrl(forecastApiUrl(), Map.of(QUERY_PARAM, city));
    }

    public String buildWeatherUrlForZipcode(Integer zipcode, String country) {
        return buildUrl(weatherApiUrl(), Map.of(ZIP_PARAM, zipcode + "," + country));
    }

    public String buildWeatherUrlForCoord(double lat, double lon) {
        return buildUrl(weatherApiUrl(), Map.of(LAT_PARAM, lat, LON_PARAM, lon));
    }

    private String buildUrl(String apiUrl, Map<String, Object> queryParams) {
        UriBuilder uriBuilder = UriBuilder
                .fromUri(apiUrl)
                .queryParam(API_KEY_PARAM, apiKey());

        queryParams.forEach(uriBuilder::queryParam);

        return uriBuilder.build().toString();
    }

    private String weatherApiUrl() {
        return generalSettings.getOpenWeatherApi().weatherUrl();
    }

    private String forecastApiUrl() {
        return generalSettings.getOpenWeatherApi().forecastUrl();
    }

    private String apiKey() {
        return generalSettings.getOpenWeatherApi().key();
    }

}
