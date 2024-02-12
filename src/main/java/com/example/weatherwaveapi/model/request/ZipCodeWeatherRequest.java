package com.example.weatherwaveapi.model.request;

import lombok.Builder;

@Builder
public record ZipCodeWeatherRequest(
        Integer zipcode,
        String country
) {
}
