package com.example.weatherwaveapi.model.response.weatherapi.forecast;

import com.example.weatherwaveapi.model.response.weatherapi.forecast.ForecastData;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;


import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherForecastResponse {
    @Builder.Default
    Boolean success = true;
    String errorMessage;
    String city;
    String country;
    List<ForecastData> forecastData;

}
