package com.example.weatherwaveapi.model.response.weatherapi.weather;

public record SunActivityInfo(
       Integer type,
       Integer id,
       String country,
       Long sunrise,
       Long sunset
) {
}
