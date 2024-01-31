package com.example.weatherwaveapi.model.request;

import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import lombok.Builder;

import java.util.List;

@Builder
public record WeatherRequest(
        List<String> cities,
        ServiceApiEnum service
) {
}
