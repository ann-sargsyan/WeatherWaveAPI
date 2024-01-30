package com.example.weatherwaveapi.model.response.weatherapi.weather.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Wind(
        double speed,
        @JsonProperty("deg") int degrees,
        double gust
) {
}
