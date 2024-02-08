package com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Clouds(
        @JsonProperty("all") Integer cloudiness
) {
}
