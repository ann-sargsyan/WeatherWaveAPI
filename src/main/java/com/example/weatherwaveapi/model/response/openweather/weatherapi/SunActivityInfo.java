package com.example.weatherwaveapi.model.response.openweather.weatherapi;

import lombok.Builder;

@Builder
public record SunActivityInfo(
       Integer type,
       Integer id,
       String country,
       Long sunrise,
       Long sunset
) {
}
