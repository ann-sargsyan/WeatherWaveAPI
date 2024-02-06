package com.example.weatherwaveapi.model.request;

import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record WeatherRequest(
        List<String> cities,
        Map<Integer, String> zipCodeCountryMap,
        ServiceApiEnum service
) {
}