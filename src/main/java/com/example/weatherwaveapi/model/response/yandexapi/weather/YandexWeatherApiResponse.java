package com.example.weatherwaveapi.model.response.yandexapi.weather;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class YandexWeatherApiResponse{
    @Builder.Default
    Boolean success = true;
    String errorMessage;
    Double latitude;
    Double longitude;
    Double temperature;
    String condition;
    String date;
}
