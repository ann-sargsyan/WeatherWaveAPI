package com.example.weatherwaveapi.model.response.weatherapi;

public record SunActivityInfo(
       Integer type,
       Integer id,
       String country,
       Long sunrise,
       Long sunset
) {
}
