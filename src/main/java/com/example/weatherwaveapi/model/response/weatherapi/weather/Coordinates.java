package com.example.weatherwaveapi.model.response.weatherapi.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Coordinates(
        @JsonProperty("lon") Double longitude,
        @JsonProperty("lat") Double latitude
) {
}
