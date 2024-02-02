package com.example.weatherwaveapi.model.response;

import lombok.Builder;


@Builder
public record WeatherApiResponse(
        Boolean success,
        String city,
        String country,
        Double temperature,
        String weatherDescription
) {
}
