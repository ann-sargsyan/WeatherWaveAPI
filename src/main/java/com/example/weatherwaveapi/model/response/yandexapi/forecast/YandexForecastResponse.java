package com.example.weatherwaveapi.model.response.yandexapi.forecast;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class YandexForecastResponse{
    @Builder.Default
    Boolean success = true;
    String errorMessage;
    Double latitude;
    Double longitude;
    String date;
    Integer week;
    List<ForecastPartResponse> forecast;
}
