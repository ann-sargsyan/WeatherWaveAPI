package com.example.weatherwaveapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.settings")
public class GeneralSettings {

    private OpenWeather openWeatherMap;
    private Yandex yandexMap;
    private RestTemplate restTemplateProperties;

    @Data
    public static class OpenWeather {
        private Api api;

        @Data
        public static class Api {
            private String key;
            private String weatherUrl;
            private String forecastUrl;
        }
    }

    @Data
    public static class Yandex {
        private Api api;

        @Data
        public static class Api {
            private String key;
            private String url;
        }
    }

    @Data
    public static class RestTemplate {
        private Integer readTimeout;
        private Integer connectionTimeout;
    }

}

