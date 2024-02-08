package com.example.weatherwaveapi.model.response.openweather.weatherapi.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record Weather(
        Integer id,
        @JsonProperty("main") String groupOfWeatherParameters,
        String description,
        String icon
) {
}
