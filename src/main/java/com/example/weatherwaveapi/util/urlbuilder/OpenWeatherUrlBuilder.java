package com.example.weatherwaveapi.util.urlbuilder;

import com.example.weatherwaveapi.config.GeneralSettings;
import jakarta.ws.rs.core.UriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenWeatherUrlBuilder {
    private final String apiKey;
    private final String weatherApiUrl;
    private final String forecastApiUrl;

    @Autowired
    public OpenWeatherUrlBuilder(GeneralSettings generalSettings) {
        this.apiKey = generalSettings.getOpenWeatherMap().getApi().getKey();
        this.weatherApiUrl = generalSettings.getOpenWeatherMap().getApi().getWeatherUrl();
        this.forecastApiUrl = generalSettings.getOpenWeatherMap().getApi().getForecastUrl();
    }

    public String buildWeatherUrl(String str) {
        return UriBuilder.fromUri(weatherApiUrl)
                .queryParam("q", str)
                .queryParam("appid", apiKey)
                .build()
                .toString();
    }

    public String buildForecastUrlForCity(String city) {
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
