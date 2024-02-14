package com.example.weatherwaveapi.model.response.yandexapi.forecast;

import lombok.Builder;

@Builder
public record ForecastPartResponse(
        String dayTime,
        Double temperature,
        String condition
) {
}
