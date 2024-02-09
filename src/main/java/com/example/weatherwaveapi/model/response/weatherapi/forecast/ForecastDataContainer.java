package com.example.weatherwaveapi.model.response.weatherapi.forecast;


import com.example.weatherwaveapi.model.response.weatherapi.weather.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
@Builder
public record ForecastDataContainer(
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
        public List<String> extractWeatherDescriptions() {
                return weather.stream()
                        .map(Weather::description)
                        .toList();
        }
}
