package com.example.weatherwaveapi.model.request;

import java.util.List;

public record YandexWeatherRequest(
        List<CoordinateWeatherRequest> coordinates
) {
}
