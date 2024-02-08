package com.example.weatherwaveapi.model.response.openweather;


import com.example.weatherwaveapi.model.response.openweather.forecastapi.ForecastData;
import com.example.weatherwaveapi.model.response.openweather.forecastapi.ForecastDataContainer;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.City;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.Coordinates;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.SunActivityInfo;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.WeatherMetrics;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.Weather;

import com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.type.Clouds;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.type.Rain;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.type.Wind;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record WeatherOpenApiContainer(
        Boolean success,
        String errorMessage,
        @JsonProperty("coord")
        Coordinates coordinates,
        List<Weather> weather,
        String base,
        @JsonProperty("main")
        WeatherMetrics weatherMetrics,
        Integer visibility,
        Wind wind,
        Rain rain,
        Clouds clouds,
        @JsonProperty("dt")
        Long dataTime,
        @JsonProperty("sys")
        SunActivityInfo sunActivityInfo,
        Integer timezone,
        @JsonProperty("id")
        Integer cityId,
        @JsonProperty("name")
        String cityName,
        @JsonProperty("cod")
        Integer internalParameter,
        @JsonProperty("city")
        City cityDetails,
        @JsonProperty("list")
        List<ForecastDataContainer> forecastData
) {
        public List<ForecastData> forecastContainer(){
                return forecastData.stream()
                        .map(c -> ForecastData.builder()
                                .date(c.date())
                                .description(c.extractWeatherDescriptions())
                                .temperature(c.weatherMetrics().temp())
                                .build())
                        .toList();
        }
}
