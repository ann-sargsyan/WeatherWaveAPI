package com.example.weatherwaveapi.service;

import com.example.weatherwaveapi.util.urlbuilder.OpenWeatherUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {
    private final OpenWeatherUrlBuilder urlBuilder;
    private final RestTemplate restTemplate;

    public Object getWeatherBySelectedCity(String city) {
        String url = urlBuilder.buildWeatherUrl(city);
        return restTemplate.getForObject(url, Object.class);
    public WeatherOpenApiContainer getWeatherBySelectedCity(String city) {
        String url = getUrlForSelectedCity(city);
        try {
            ResponseEntity<WeatherOpenApiContainer> responseEntity = restTemplate.exchange(
                    url,
                    GET,
                    new HttpEntity<>(new HttpHeaders()),
                    new ParameterizedTypeReference<>() {
                    }
            );
            return responseEntity.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error(EXCEPTION_MESSAGE, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public WeatherResponse getWeather(WeatherRequest request) {

        List<WeatherApiResponse> weatherResponses = new ArrayList<>();
        for (String city : request.cities()) {
            WeatherOpenApiContainer weatherApiResponse = getWeatherBySelectedCity(city);
            weatherResponses.add(convertContainer(weatherApiResponse));
        }

        return WeatherResponse.builder()
                .responses(weatherResponses)
                .build();
    }

    private String getUrlForSelectedCity(String city) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", city)
                .queryParam("appid", apiKey);

        return builder.toUriString();
    }

    private WeatherApiResponse convertContainer(WeatherOpenApiContainer container) {
        return WeatherApiResponse.builder()
                .success(true)
                .temperature(container.weatherMetrics().temp())
                .city(container.name())
                .country(container.sunActivityInfo().country())
                .weatherDescription(container.weather().get(0).description())
                .build();

    }
}

