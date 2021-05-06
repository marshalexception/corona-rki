package com.sitrumm.coronarki.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitrumm.coronarki.model.DayCountryEntity;
import com.sitrumm.coronarki.model.SummaryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CovidService {

    @Value("${covid.basepath}")
    private String basePath;

    @Value("${covid.path.dayone.bycountry}")
    private String countryDayOnePath;

    @Value("${covid.path.summary}")
    private String summaryPath;

    private final RestTemplate restTemplate;

    public CovidService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public SummaryEntity getCovidSummary() {
        ResponseEntity<SummaryEntity> response = restTemplate.getForEntity(basePath + summaryPath, SummaryEntity.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        } else {
            return new SummaryEntity();
        }
    }

    public List<DayCountryEntity> getDataByCountryDayOne(String country) {
        ResponseEntity<String> response = restTemplate.getForEntity(basePath + countryDayOnePath + country, String.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return buildDataByCountryResponse(response.getBody());
        } else {
            return new ArrayList<>();
        }
    }

    private List<DayCountryEntity> buildDataByCountryResponse(String body) {
        List<DayCountryEntity> rkiData = null;
        try {
            rkiData = new ObjectMapper().readValue(body, new TypeReference<List<DayCountryEntity>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rkiData;
    }

}
