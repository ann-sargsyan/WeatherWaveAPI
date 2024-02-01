package com.example.weatherwaveapi.config;

public record RestTemplateProperties(
        int readTimeout,
        int connectionTimeout
) {
}
