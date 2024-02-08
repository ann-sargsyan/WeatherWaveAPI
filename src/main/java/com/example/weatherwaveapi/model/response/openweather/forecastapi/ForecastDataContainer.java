package com.example.weatherwaveapi.model.response.openweather.forecastapi;

import com.example.weatherwaveapi.model.response.openweather.weatherapi.WeatherMetrics;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.Weather;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.type.Clouds;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.type.Rain;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.type.Wind;
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
