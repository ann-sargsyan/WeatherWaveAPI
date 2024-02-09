package com.example.weatherwaveapi.model.response;

import lombok.Builder;


@Builder
public record WeatherApiResponse(
        String city,
        String country,
        double temperature,
        String weatherDescription
) {
}
