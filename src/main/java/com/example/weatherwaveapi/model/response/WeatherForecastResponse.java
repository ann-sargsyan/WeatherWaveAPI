package com.example.weatherwaveapi.model.response;

import com.example.weatherwaveapi.model.response.openweather.forecastapi.ForecastData;
import lombok.Builder;


import java.util.List;
@Builder
public record WeatherForecastResponse(
        Boolean success,
        String city,
        String country,
        List<ForecastData> forecastData
) {
}
