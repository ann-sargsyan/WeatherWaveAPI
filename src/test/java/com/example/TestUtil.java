package com.example;

import com.example.weatherwaveapi.model.response.openweather.WeatherOpenApiContainer;
import com.example.weatherwaveapi.model.response.openweather.forecastapi.ForecastDataContainer;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.City;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.SunActivityInfo;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.WeatherMetrics;
import com.example.weatherwaveapi.model.response.openweather.weatherapi.weather.Weather;

import java.util.List;

public class TestUtil {
    public static final String URL = "http://localhost:8080/weather/city=london";
    public static final String CITY_LONDON = "london";
    public static final Integer ZIPCODE = 94040;
    public static final String COUNTRY_US = "US";
    public static final double TEMPERATURE = 35D;
    public static final int VISIBILITY = 10000;
    public static final String COUNTRY_GB = "GB";
    public static final String DESCRIPTION = "light intensity drizzle";
    public static final String DATE = "2024-02-12 06:00:00";


    public static WeatherOpenApiContainer mockContainerForWeather = WeatherOpenApiContainer.builder()
            .weatherMetrics(WeatherMetrics.builder().temp(TEMPERATURE).build())
            .visibility(VISIBILITY)
            .sunActivityInfo(SunActivityInfo.builder().country(COUNTRY_GB).build())
            .cityName(CITY_LONDON)
            .weather(List.of(Weather.builder().description(DESCRIPTION).build()))
            .build();

    public static WeatherOpenApiContainer mockContainerForForecast = WeatherOpenApiContainer.builder()
            .cityDetails(City.builder()
                    .name(CITY_LONDON)
                    .country(COUNTRY_GB).build())
            .forecastData(List.of(ForecastDataContainer.builder()
                            .date(DATE)
                            .weatherMetrics(WeatherMetrics.builder().temp(TEMPERATURE).build())
                            .weather(List.of(Weather.builder().description(DESCRIPTION).build()))
                    .build()))
            .build();
}
