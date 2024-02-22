package com.example.weatherwaveapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                description = "WeatherWaveAPI is an API designed to retrieve weather information " +
                        "from different sources, namely Yandex and OpenWeather.",
                title = "WeatherWaveAPI",
                version = "1.0"
        )
)
public class OpenApiConfig {
}
