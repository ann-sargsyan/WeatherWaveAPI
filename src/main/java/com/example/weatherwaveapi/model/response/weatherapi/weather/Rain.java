package com.example.weatherwaveapi.model.response.weatherapi.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Rain(
        @JsonProperty("1h") Double oneHour
) {

}
