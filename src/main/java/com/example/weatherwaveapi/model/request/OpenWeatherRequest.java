package com.example.weatherwaveapi.model.request;

import lombok.Builder;

import java.util.List;

@Builder
public record OpenWeatherRequest(
        List<String> cities,
        List<ZipCodeWeatherRequest> zipcode
) {
}
