package com.example.weatherwaveapi.model.response;

import com.example.weatherwaveapi.model.response.weatherapi.City;
import com.example.weatherwaveapi.model.response.weatherapi.WeatherMetrics;
import com.example.weatherwaveapi.model.response.weatherapi.weather.Weather;
import com.example.weatherwaveapi.model.response.weatherapi.weather.type.Clouds;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


import java.util.List;
@Builder
public record WeatherForecastResponse(
        Boolean success,
        String city,
        String country,
        List<ForecastData> weather,
        String date
) {

}
