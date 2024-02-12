package com.example.weatherwaveapi.model.response.weatherapi.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Wind(
        Double speed,
        @JsonProperty("deg") Integer degrees,
        Double gust
) {
}
