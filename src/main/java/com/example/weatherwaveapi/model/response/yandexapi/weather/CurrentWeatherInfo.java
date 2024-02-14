package com.example.weatherwaveapi.model.response.yandexapi.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CurrentWeatherInfo(
        Double temp,
        @JsonProperty("feelsLike")
        Double feelsLike,
        @JsonProperty
        Double temp_water,
        String icon,
        String condition,
        @JsonProperty("wind_speed")
        Double windSpeed,
        @JsonProperty("wind_gust")
        Double windGustSpeed,
        @JsonProperty("wind_dir")
        String windDir,
        @JsonProperty("pressure_mm")
        Double pressureInMm,
        @JsonProperty("pressure_pa")
        Double pressureInPa,
        Integer humidity,
        String daytime,
        Boolean polar,
        String season,
        @JsonProperty("obs_time")
        Long observationTime
) {
}
