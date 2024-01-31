package com.example.weatherwaveapi.model.response.weatherapi;

import com.example.weatherwaveapi.model.response.weatherapi.weather.Weather;
import com.example.weatherwaveapi.model.response.weatherapi.weather.type.Clouds;
import com.example.weatherwaveapi.model.response.weatherapi.weather.type.Rain;
import com.example.weatherwaveapi.model.response.weatherapi.weather.type.Wind;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record WeatherOpenApiContainer(
        @JsonProperty("coord") Coordinates coordinates,
        List<Weather> weather,
        String base,
        @JsonProperty("main") WeatherMetrics weatherMetrics,
        int visibility,
        Wind wind,
        Rain rain,
        Clouds clouds,
        long dt,
        @JsonProperty("sys") SunActivityInfo sunActivityInfo,
        int timezone,
        int id,
        String name,
        int cod
) {
}
