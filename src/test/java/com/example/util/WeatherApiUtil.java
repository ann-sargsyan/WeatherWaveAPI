package com.example.util;

import com.example.weatherwaveapi.model.request.YandexWeatherRequest;
import com.example.weatherwaveapi.model.response.weatherapi.WeatherOpenApiContainer;
import com.example.weatherwaveapi.model.response.weatherapi.forecast.City;
import com.example.weatherwaveapi.model.response.weatherapi.forecast.ForecastDataContainer;
import com.example.weatherwaveapi.model.response.weatherapi.weather.SunActivityInfo;
import com.example.weatherwaveapi.model.response.weatherapi.weather.Weather;
import com.example.weatherwaveapi.model.response.weatherapi.weather.WeatherMetrics;
import com.example.weatherwaveapi.model.response.yandexapi.YandexApiContainer;
import com.example.weatherwaveapi.model.response.yandexapi.forecast.Forecast;
import com.example.weatherwaveapi.model.response.yandexapi.forecast.ForecastPart;
import com.example.weatherwaveapi.model.response.yandexapi.weather.CurrentWeatherInfo;
import com.example.weatherwaveapi.model.response.yandexapi.weather.LocalityInfo;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class WeatherApiUtil {
    public static final String LONDON = "London";
    public static final String COUNTRY_OF_LONDON = "GB";
    public static final Double LONDON_TEMPERATURE = 276.79;
    public static final String LONDON_CLOUDS = "overcast clouds";
    public static final String YEREVAN = "Yerevan";
    public static final String COUNTRY_OF_YEREVAN = "AM";
    public static final Double YEREVAN_TEMPERATURE = 271.24;
    public static final String YEREVAN_CLOUDS = "scattered clouds";
    public static final Integer ZIPCODE = 94040;
    public static final String COUNTRY_USA = "US";
    public static final String DATE = "2024-02-12 06:00:00";
    public static final String DESCRIPTION = "light intensity drizzle";
    public static final Double MOSCOW_LAT = 55.75396;
    public static final Double MOSCOW_LON = 37.620393;
    public static final Integer WEEK_NUMBER = 7;
    public static final String DAY_TIME = "morning";
    public static final Double MOSCOW_TEMPERATURE = 15.5;
    public static final String INVALID_COORDINATES_MESSAGE_FORMAT = "Invalid coordinates: %s. %s";
    public static final String WEATHER_ERROR_MESSAGE = "Failed to retrieve weather data";
    public static final String LOCALHOST = "http://localhost:";
    public static final String SKIP = "";

    public YandexApiContainer getContainerForYandexWeather() {
        return YandexApiContainer.builder()
                .info(LocalityInfo.builder()
                        .lon(MOSCOW_LON)
                        .lat(MOSCOW_LAT)
                        .build())
                .serverTimeUTC(DATE)
                .weatherInfo(CurrentWeatherInfo.builder()
                        .temp(MOSCOW_TEMPERATURE)
                        .condition(DESCRIPTION)
                        .build())
                .build();
    }

    public YandexApiContainer getContainerForYandexForecast() {
        return YandexApiContainer.builder()
                .info(LocalityInfo.builder()
                        .lon(MOSCOW_LON)
                        .lat(MOSCOW_LAT)
                        .build())
                .forecast(Forecast.builder()
                        .date(DATE)
                        .week(WEEK_NUMBER)
                        .parts(List.of(ForecastPart.builder()
                                .daytime(DAY_TIME)
                                .tempAvg(MOSCOW_TEMPERATURE)
                                .condition(DESCRIPTION)
                                .build()))
                        .build())
                .build();
    }

    public YandexApiContainer getErrorContainerForYandexWeather(YandexWeatherRequest request) {
        return YandexApiContainer
                .builder()
                .errorMessage(String.format(INVALID_COORDINATES_MESSAGE_FORMAT, request, WEATHER_ERROR_MESSAGE))
                .build();
    }

    public WeatherOpenApiContainer getContainerForOpenWeather() {
        return WeatherOpenApiContainer
                .builder()
                .weatherMetrics(WeatherMetrics.builder()
                        .temp(YEREVAN_TEMPERATURE)
                        .build())
                .cityName(YEREVAN)
                .sunActivityInfo(SunActivityInfo
                        .builder()
                        .country(COUNTRY_OF_YEREVAN)
                        .build())
                .weather(List.of(Weather.builder()
                        .description(YEREVAN_CLOUDS)
                        .build()))
                .build();
    }

    public WeatherOpenApiContainer getContainerForOpenWeatherForecast() {
        return WeatherOpenApiContainer.builder()
                .cityDetails(City.builder()
                        .name(LONDON)
                        .country(COUNTRY_OF_LONDON).build())
                .forecastData(List.of(ForecastDataContainer.builder()
                        .date(DATE)
                        .weatherMetrics(WeatherMetrics.builder().temp(LONDON_TEMPERATURE).build())
                        .weather(List.of(Weather.builder().description(LONDON_CLOUDS).build()))
                        .build()))
                .build();
    }
}
