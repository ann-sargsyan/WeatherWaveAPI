package com.example.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class WeatherApiUtil {
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
    public static final Double USA_TEMPERATURE = 35D;
    public static final Double MOSCOW_LAT = 55.75396;
    public static final Double MOSCOW_LON = 37.620393;
    public static final Integer WEEK_NUMBER = 7;
    public static final String DAY_TIME = "morning";
    public static final Double MOSCOW_TEMPERATURE = 15.5;
    public static final String INVALID_COORDINATES_MESSAGE_FORMAT = "Invalid coordinates: %s. %s";
    public static final String WEATHER_ERROR_MESSAGE = "Failed to retrieve weather data";
}
