package com.example.weatherwaveapi.config;

import org.springframework.beans.factory.annotation.Value;

public record OpenWeatherApi(
        String key,
        String weatherUrl,
        String forecastUrl
) {
}
