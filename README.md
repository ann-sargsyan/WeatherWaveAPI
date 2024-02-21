# WeatherWaveAPI
WeatherWaveAPI is an API designed to retrieve weather information from different sources,
namely Yandex and OpenWeather. This API provides users with the ability to access current
weather data based on city, zipcode (from OpenWeather), or coordinates (from Yandex).
Additionally, users can fetch current weather information from both APIs simultaneously
for comparison purposes.
The API also supports retrieving current weather information for multiple
locations and obtaining forecast data based on either city or coordinates.

## Features
* Retrieve current weather information from OpenWeather based on city or zipcode.
* Retrieve current weather information from Yandex based on coordinates.
* Obtain current weather information for multiple locations.

## Installation Guide
To get started with WeatherWaveAPI, follow these steps:
1. Clone the repository to your local machine.

 ```bash
    git clone https://github.com/ann-sargsyan/WeatherWaveAPI.git
```

2. Navigate to the project directory.

 ```bash
    cd WeatherWaveAPI
```

3. Build the project using Maven.

 ```bash
mvn clean install
 ```

4. Set an active profile and run the application

 ```bash
java -jar -Dspring.profiles.active=development target/WeatherWaveAPI_-0.0.1-SNAPSHOT.jar
```

## Usage
Once the application is up and running, you can interact with the API using the provided endpoints.
Detailed information about each endpoint and expected responses are documented in Swagger.

#### Swagger UI URL: http://localhost:8080/swagger-ui.html

## Used Technologies

* Java
* Spring Boot
* Maven
* OpenWeather API
* Yandex Weather API

## API Documentation

### Get Weather

```bash lines
GET /weather/params
```
### Summary
Retrieve current weather information from OpenWeather and Yandex.

### Request Parameters
- **cities:** (Optional) A list of city names for OpenWeatherAPI.
- **zipCodes:** (Optional) A list of zip codes for OpenWeatherAPI. Must contain 5 digits.
- **coordinates:** (Optional) A list of coordinates valid for YandexAPI. Required fields are latitude and longitude in degrees.
- **serviceApi:** (Optional) Service API to use, defaults to OpenWeatherAPI. Can be either "OPEN_WEATHER_MAP" or "YANDEX".

### Request Example

- **Based on cities:**
```bash
curl -X GET "http://localhost:8080/weather/params?cities=London,Paris&serviceApi=OPEN_WEATHER_MAP" -H "Content-Type: application/json"
```

- **Based on zipcodes:**
```bash
curl -X 'GET' 'http://localhost:8080/weather/params?zipCodes=45011%2Cus&serviceApi=OPEN_WEATHER_MAP' -H 'accept: application/json'
```
- **Based on coordinates:**
```bash
curl -X 'GET' 'http://localhost:8080/weather/params?coordinates=45%2C%2035&serviceApi=YANDEX' -H 'accept: application/json'
``` 
## Response

#### success
- **HTTP Response:**

```200 OK```
- **Content Type:**

`` application/json``

- **Fields in API response:**

    - **`yandexWeatherResponse` (Object):**
        - `responses` (Array): Yandex weather information.
            - `errorMessage` (String): Error message for the Yandex response in case of invalid parameters.
            - `latitude` (Number): Latitude value.
            - `longitude` (Number): Longitude value.
            - `temperature` (Number): Temperature value.
            - `condition` (String): Weather condition.
            - `date` (String): Date information.

        - `serviceApiEnum` (Enum): Indicates the service API used for the response. Enum: "YANDEX"

    - **`openWeatherResponse` (Object):**
        - `responses` (Array): OpenWeather information.
            - `errorMessage` (String): Error message for the OpenWeather response in case of invalid response.
            - `city` (String): City name.
            - `country` (String): Country name.
            - `temperature` (Number): Temperature value.
            - `weatherDescription` (String): Description of the weather.

        - `serviceApiEnum` (Enum): Indicates the service API used for the response. Enum: "OPEN_WEATHER_MAP"

    - **`serviceApiEnum` (String):**
        - Enum: "YANDEX", "OPEN_WEATHER_MAP"
        - Description: Indicates the primary service API used for the response. By default, is OPEN_WEATHER_MAP



- **Response Body:**

##### Response body example for one location

example 1: from OpenWeather
```json
{
  "openWeatherResponse": {
    "responses": [
      {
        "city": "London",
        "country": "GB",
        "temperature": 293.66,
        "weatherDescription": "few clouds"
      }
    ]
  }
}
```
example 2: from Yandex
```json
{
  "yandexWeatherResponse": {
    "responses": [
      {
        "latitude": 34,
        "longitude": 45,
        "temperature": 15,
        "condition": "clear",
        "date": "2024-02-21T07:00:55.741126Z"
      }
    ]
  }
}
```
example 3: In case of invalid argument
```json
{
  "openWeatherResponse": {
    "responses": [
      {
        "errorMessage": "Search string: mscow, error: Failed to retrieve weather data"
      }
    ]
  }
}
```

##### Response body example for multiple locations
example 1: from OpenWeather
```json
{
  "openWeatherResponse": {
    "responses": [
      {
        "city": "London",
        "country": "GB",
        "temperature": 282.04,
        "weatherDescription": "moderate rain"
      },
      {
        "city": "Moscow",
        "country": "RU",
        "temperature": 267.39,
        "weatherDescription": "overcast clouds"
      }
    ]
  }
}
```
example 2: from Yandex
```json
{
  "yandexWeatherResponse": {
    "responses": [
      {
        "latitude": 32,
        "longitude": 35,
        "temperature": 15,
        "condition": "clear",
        "date": "2024-02-21T07:05:18.435340Z"
      },
      {
        "latitude": 46,
        "longitude": 24,
        "temperature": 2,
        "condition": "overcast",
        "date": "2024-02-21T07:05:18.511223Z"
      }
    ]
  }
}
```
example 3: In case of invalid argument
```json
{
  "openWeatherResponse": {
    "responses": [
      {
        "city": "London",
        "country": "GB",
        "temperature": 282.04,
        "weatherDescription": "moderate rain"
      },
      {
        "errorMessage": "Search string: mscow, error: Failed to retrieve weather data"
      }
    ]
  }
}
```
