package com.example.weatherwaveapi;

import com.example.weatherwaveapi.config.GeneralSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {GeneralSettings.class})
public class WeatherWaveApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherWaveApiApplication.class, args);
    }
}
