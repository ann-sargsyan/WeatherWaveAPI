package com.example.weatherwaveapi.model.response;

import lombok.Builder;


@Builder(toBuilder = true)
public record WeatherApiResponse(
        boolean success,
        String city,
        String country,
        double temperature,
        String weatherDescription
) {
}
