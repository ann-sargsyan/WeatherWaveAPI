package com.example.weatherwaveapi.model.response.yandexapi.weather;

import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record YandexWeatherResponse(
        List<YandexWeatherApiResponse> responses,
        ServiceApiEnum serviceApiEnum
) {
}
