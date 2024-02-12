package com.example.weatherwaveapi.config;

public record OpenWeatherApi(
        String key,
        String weatherUrl,
        String forecastUrl
) {
}
