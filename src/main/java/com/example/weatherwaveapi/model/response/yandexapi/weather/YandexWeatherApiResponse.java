package com.example.weatherwaveapi.model.response.yandexapi.weather;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record YandexWeatherApiResponse(
        Boolean success,
        String errorMessage,
        Double latitude,
        Double longitude,
        Double temperature,
        String condition,
        String date
) {
}
