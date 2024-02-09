package com.example.weatherwaveapi.model.response.weatherapi.weather;

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
