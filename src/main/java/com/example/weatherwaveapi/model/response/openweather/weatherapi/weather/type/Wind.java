package com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Wind(
        Double speed,
        @JsonProperty("deg") int degrees,
        Double gust
) {
}
