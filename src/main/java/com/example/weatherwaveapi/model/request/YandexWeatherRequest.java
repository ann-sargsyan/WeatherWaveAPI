package com.example.weatherwaveapi.model.request;

import com.example.weatherwaveapi.serviceapienum.ServiceApiEnum;
import lombok.Builder;

import java.util.List;

@Builder
public record YandexWeatherRequest(
        List<CoordinateWeatherRequest> coordinates,
        ServiceApiEnum service
) {
}
