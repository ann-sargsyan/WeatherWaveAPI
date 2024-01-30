package com.example.weatherwaveapi.model.response.weatherapi.weather.type;

public record Wind(
        double speed,
        int deg,
        double gust
) {
}
