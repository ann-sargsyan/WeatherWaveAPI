package com.example.weatherwaveapi.model.response.weatherapi.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Clouds(
        @JsonProperty("all") Integer cloudiness
) {
}
