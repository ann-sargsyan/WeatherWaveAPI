package com.example.weatherwaveapi.model.response.weatherapi.weather;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WeatherApiResponse(
        String errorMessage,
        String city,
        String country,
        Double temperature,
        String weatherDescription
) {
}
