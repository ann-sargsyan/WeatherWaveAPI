package com.example.weatherwaveapi.model.request;

import lombok.Builder;

@Builder
public record CoordinateWeatherRequest(
        Double lat,
        Double lon
) {
}
