package com.example.weatherwaveapi.model.response.yandexapi.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record YandexForecastResponse(
        Boolean success,
        String errorMessage,
        Double latitude,
        Double longitude,
        String date,
        Integer week,
        List<ForecastPartResponse> forecast
) {
}
