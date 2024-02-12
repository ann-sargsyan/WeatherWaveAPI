package com.example.weatherwaveapi.model.response.weatherapi.weather;

public record Wind(
        Double speed,
        Integer deg,
        Double gust
) {
}
