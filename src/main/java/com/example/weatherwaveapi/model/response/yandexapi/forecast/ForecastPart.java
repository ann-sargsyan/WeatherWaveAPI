package com.example.weatherwaveapi.model.response.yandexapi.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ForecastPart(
        @JsonProperty("part_name")
        String timeOfTheDay,
        @JsonProperty("temp_min")
        Double tempMin,
        @JsonProperty("temp_max")
        Double tempMax,
        @JsonProperty("temp_avg")
        Double tempAvg,
        @JsonProperty("feels_like")
        Double feelsLike,
        String icon,
        String condition,
        String daytime,
        Boolean polar,
        @JsonProperty("wind_speed")
        Double windSpeed,
        @JsonProperty("wind_gust")
        Double windGust,
        @JsonProperty("wind_dir")
        String windDir,
        @JsonProperty("pressure_mm")
        Double pressureInMm,
        @JsonProperty("pressure_pa")
        Double pressureInPa,
        Integer humidity,
        @JsonProperty("prec_mm")
        Double predictedPrecipitation,
        @JsonProperty("prec_period")
        Integer precipitationPeriod,
        @JsonProperty("prec_prob")
        Double precipitationChance
) {
}
