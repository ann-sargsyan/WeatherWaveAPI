package com.example.weatherwaveapi.model.response.yandexapi.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record Forecast(
        String date,
        @JsonProperty("date_ts")
        Long forecastDateUnix,
        Integer week,
        String sunrise,
        String sunset,
        @JsonProperty("moon_code")
        Integer lunarPhaseCode,
        @JsonProperty("moon_text")
        String lunarPhaseTextCode,
        List<ForecastPart> parts

) {

    public List<ForecastPartResponse> extractForecastInfo() {
        return parts.stream()
                .map(x -> ForecastPartResponse.builder()
                        .dayTime(x.timeOfTheDay())
                        .temperature(x.tempAvg())
                        .condition(x.condition())
                        .build()).toList();
    }
}
