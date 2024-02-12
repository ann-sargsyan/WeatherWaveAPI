package com.example.weatherwaveapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
@RequiredArgsConstructor
@Service
public class ServiceImpl {
    private final RestTemplate restTemplate;
    private final String apiKey = "13cd7771-c622-4f86-a1fb-39abad43ce2a";

    public Object getWeather() {
        String url = "https://api.weather.yandex.ru/v2/informers?lat=55.75396&lon=37.620393";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-Yandex-API-Key", apiKey);

//        HttpEntity<String> e = new HttpEntity<>("", headers);
        System.out.println(headers.get("X-Yandex-API-Key"));
        try {
            return restTemplate.getForObject(url, Object.class, headers);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
