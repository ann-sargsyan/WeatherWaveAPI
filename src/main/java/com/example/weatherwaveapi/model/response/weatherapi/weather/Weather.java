package com.example.weatherwaveapi.model.response.weatherapi.weather;

public record Weather(
        Integer id,
        String main,
        String description,
        String icon
) {
}
