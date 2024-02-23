package com.example.weatherwaveapi.model.response.weatherapi.weather;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherApiResponse {
    @Builder.Default
    Boolean success = true;
    String errorMessage;
    String city;
    String country;
    Double temperature;
    String weatherDescription;

}
