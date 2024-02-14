package com.example.weatherwaveapi.model.response.weatherapi.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record WeatherMetrics(
        Double temp,
        @JsonProperty("feelsLike") Double feelsLike,
        @JsonProperty("temp_min") Double tempMin,
        @JsonProperty("temp_max") Double tempMax,
        Integer pressure,
        Integer humidity,
        @JsonProperty("sea_level") Integer seaLevel,
        @JsonProperty("grnd_level") Integer grndLevel
) {
}
