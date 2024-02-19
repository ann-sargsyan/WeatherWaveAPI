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


    git clone https://github.com/ann-sargsyan/WeatherWaveAPI.git

2. Navigate to the project directory.


    cd WeatherWaveAPI

3. Build the project using Maven.


    mvn clean install

4. Set an active profile and run the application


    java -jar -Dspring.profiles.active=development target/WeatherWaveAPI_-0.0.1-SNAPSHOT.jar

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
