package com.example.weatherwaveapi.model.response.weatherapi;

import com.example.weatherwaveapi.model.response.weatherapi.weather.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record WeatherOpenApiContainer(
        String errorMessage,
        @JsonProperty("coord") Coordinates coordinates,
        List<Weather> weather,
        String base,
        @JsonProperty("main") WeatherMetrics weatherMetrics,
        Integer visibility,
        Wind wind,
        Rain rain,
        Clouds clouds,
        @JsonProperty("dt") Long dataTime,
        @JsonProperty("sys") SunActivityInfo sunActivityInfo,
        Integer timezone,
        @JsonProperty("id") Integer cityId,
        @JsonProperty("name") String cityName,
        @JsonProperty("cod") Integer internalParameter
) {
}
