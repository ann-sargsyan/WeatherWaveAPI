package com.example.weatherwaveapi.model.response.openweather.weatherapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record City(
        Integer id,
        String name,
        @JsonProperty("coord")
        Coordinates coordinates,
        String country,
        Integer population,
        Integer timezone,
        Long sunrise,
        Long sunset

) {
}
