package com.example.weatherwaveapi.model.response.weatherapi.forecast;

import com.example.weatherwaveapi.model.response.weatherapi.weather.Coordinates;
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
