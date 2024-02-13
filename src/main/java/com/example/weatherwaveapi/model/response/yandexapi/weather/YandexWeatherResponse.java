package com.example.weatherwaveapi.model.response.yandexapi.weather;

import lombok.Builder;

import java.util.List;

@Builder
public record YandexWeatherResponse(
        List<YandexWeatherApiResponse> responses
) {
}
