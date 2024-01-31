package com.example.weatherwaveapi.util.urlbuilder;

import jakarta.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenWeatherUrlBuilder {

    @Value("${openWeatherMap.api.key}")
    private String apiKey;

    @Value("${openWeatherMap.api.weatherUrl}")
    private String weatherApiUrl;

    @Value("${openWeatherMap.api.forecastUrl}")
    private String forecastApiUrl;

    public String buildWeatherUrl(String str) {
        return UriBuilder.fromUri(weatherApiUrl)
                .queryParam("q", str)
                .queryParam("appid", apiKey)
                .build()
                .toString();
    }

    public String buildForecastUrlForCity(String city){
        return UriBuilder.fromUri(forecastApiUrl)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .build()
                .toString();
    }

    public String buildWeatherUrlForZipcode(Integer zipcode, String country) {
        return UriBuilder.fromUri(weatherApiUrl)
                .queryParam("zip", zipcode, country)
                .queryParam("appid", apiKey)
                .build()
                .toString();
    }

    public String buildWeatherUrlForCoord(double lat, double lon) {
        return UriBuilder.fromUri(weatherApiUrl)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", apiKey)
                .build()
                .toString();
    }
}
