package com.example;

import com.example.weatherwaveapi.model.response.ForecastData;
import com.example.weatherwaveapi.model.response.weatherapi.*;
import com.example.weatherwaveapi.model.response.weatherapi.weather.Weather;
import com.example.weatherwaveapi.model.response.weatherapi.weather.type.Clouds;
import com.example.weatherwaveapi.model.response.weatherapi.weather.type.Rain;
import com.example.weatherwaveapi.model.response.weatherapi.weather.type.Wind;

import java.util.List;

public class TestUtil {
    public static final String URL = "http://localhost:8080/weather/city=london";
    public static final String CITY = "london";
    public static final Integer ZIPCODE = 94040;
    public static final String COUNTRY = "US";


    public static WeatherOpenApiContainer mockContainerForWeather = WeatherOpenApiContainer.builder()
            .success(true)
            .weatherMetrics(new WeatherMetrics(281.0, 1010D, 80D, 280.0, 280, 82, 1015, 1000))
            .visibility(10000)
            .sunActivityInfo(new SunActivityInfo(2, 5092, "GB", 1485762038L, 1485794876L))
            .cityName(CITY)
            .weather(List.of(new Weather(300, "Drizzle", "light intensity drizzle", "09d")))
            .build();

    public static WeatherOpenApiContainer mockContainerForForecast = WeatherOpenApiContainer.builder()
            .success(true)
            .list(List.of(new ForecastData(
                    new WeatherMetrics(25.0,
                            26.5,
                            22.0,
                            28.0,
                            1015,
                            60,
                            1018,
                            1010),
                    List.of(new Weather(801,
                            "Clouds",
                            "Partly cloudy",
                            "02d")),
                    new Clouds(20),
                    new Wind(10.5,
                            120,
                            12.3 ),
                    1000,
                    new Rain(1D),
                    "2024-02-06 12:00:00")
                    )
            )
            .cityDetails(City.builder()
                    .name(CITY)
                    .country(COUNTRY)
                    .build())
            .build();
}
