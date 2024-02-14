package com.example.weatherwaveapi.model.response.yandexapi.weather;

import lombok.Builder;

@Builder
public record LocalityInfo(
        Double lat,
        Double lon,
        String url
) {
}
