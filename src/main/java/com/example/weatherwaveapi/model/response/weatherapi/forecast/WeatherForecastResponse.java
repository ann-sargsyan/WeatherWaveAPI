package com.example.weatherwaveapi.model.response.weatherapi.forecast;

import com.example.weatherwaveapi.model.response.weatherapi.forecast.ForecastData;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;


import java.util.List;
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WeatherForecastResponse(
        Boolean success,
        String errorMessage,
        String city,
        String country,
        List<ForecastData> forecastData
) {
}
