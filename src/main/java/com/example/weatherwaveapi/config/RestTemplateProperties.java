package com.example.weatherwaveapi.config;

public record RestTemplateProperties(
        Long connectionTimeout,
        Long readTimeout
) {
}
