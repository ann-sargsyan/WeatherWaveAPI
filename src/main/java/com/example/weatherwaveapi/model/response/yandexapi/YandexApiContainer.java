package com.example.weatherwaveapi.model.response.yandexapi;

import com.example.weatherwaveapi.model.response.yandexapi.forecast.Forecast;
import com.example.weatherwaveapi.model.response.yandexapi.forecast.YandexForecastResponse;
import com.example.weatherwaveapi.model.response.yandexapi.weather.CurrentWeatherInfo;
import com.example.weatherwaveapi.model.response.yandexapi.weather.LocalityInfo;
import com.example.weatherwaveapi.model.response.yandexapi.weather.YandexWeatherApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Optional;

@Builder
public record YandexApiContainer(
        Boolean success,
        String errorMessage,
        @JsonProperty("now")
        Long serverTimeUnix,
        @JsonProperty("now_dt")
        String serverTimeUTC,
        LocalityInfo info,
        @JsonProperty("fact")
        CurrentWeatherInfo weatherInfo,
        Forecast forecast
) {
    public YandexWeatherApiResponse convertContainerForWeather(){
        return Optional.ofNullable(errorMessage())
                .map(error -> YandexWeatherApiResponse.builder()
                        .success(false)
                        .errorMessage(error)
                        .build())
                .orElseGet(() ->
                        YandexWeatherApiResponse.builder()
                                .latitude(info().lat())
                                .longitude(info().lon())
                                .date(serverTimeUTC())
                                .temperature(weatherInfo().temp())
                                .condition(weatherInfo().condition())
                                .build());
    }

    public YandexForecastResponse convertContainerForForecast(){
        return Optional.ofNullable(errorMessage())
                .map(error -> YandexForecastResponse.builder()
                        .success(false)
                        .errorMessage(error)
                        .build())
                .orElseGet(() ->
                        YandexForecastResponse.builder()
                                .latitude(info().lat())
                                .longitude(info().lon())
                                .date(forecast().date())
                                .week(forecast().week())
                                .forecast(forecast.extractForecastInfo())
                                .build());
    }
}
