package com.example.weatherwaveapi.model.response;

import com.example.weatherwaveapi.model.response.weatherapi.WeatherMetrics;
import com.example.weatherwaveapi.model.response.weatherapi.weather.Weather;
import com.example.weatherwaveapi.model.response.weatherapi.weather.type.Clouds;
import com.example.weatherwaveapi.model.response.weatherapi.weather.type.Rain;
import com.example.weatherwaveapi.model.response.weatherapi.weather.type.Wind;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
@Builder
public record ForecastData(
        @JsonProperty("main")
        WeatherMetrics weatherMetrics,
        List<Weather> weather,
        Clouds clouds,
        Wind wind,
        Integer visibility,
        Rain rain,
        @JsonProperty("dt_txt")
        String date
) {
}
