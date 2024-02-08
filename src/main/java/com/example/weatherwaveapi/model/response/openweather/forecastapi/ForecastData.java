package com.example.weatherwaveapi.model.response.openweather.forecastapi;

import lombok.Builder;

import java.util.List;

@Builder
public record ForecastData(
        String date,
        Double temperature,
        List<String> description
) {
}
