package com.example.weatherwaveapi.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record WeatherResponse(
        List<WeatherApiResponse> responses
) {
}
