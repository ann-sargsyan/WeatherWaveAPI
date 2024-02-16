package com.example.weatherwaveapi.model.response.weatherapi.weather;

import com.example.weatherwaveapi.model.response.weatherapi.weather.OpenWeatherResponse;
import com.example.weatherwaveapi.model.response.yandexapi.weather.YandexWeatherResponse;
import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WeatherResponse(
        YandexWeatherResponse yandexWeatherResponse,
        OpenWeatherResponse openWeatherResponse,
        ServiceApiEnum serviceApiEnum
) {
}
