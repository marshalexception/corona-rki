package com.sitrumm.coronarki;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitrumm.coronarki.model.covid.DayCountryEntity;
import com.sitrumm.coronarki.model.covid.SummaryEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class JsonDeserializationTests {

    @Test
    public void testCovidSummaryEntity() throws IOException {
        // Read file to String
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/json/summary.json")));

        // Cast it to class
        SummaryEntity rkiData = new ObjectMapper().readValue(json, SummaryEntity.class);

        // Assert
        Assertions.assertAll(
                () -> assertThat(rkiData.getId()).isNotNull(),
                () -> assertThat(rkiData.getMessage()).isNotNull(),
                () -> assertThat(rkiData.getGlobal()).isNotNull(),
                () -> assertThat(rkiData.getCountries()).isNotNull(),
                () -> assertThat(rkiData.getDate()).isNotNull()
        );
        log.info(rkiData.toString());
    }

    @Test
    public void testJsonToDayCountryEntity() throws IOException {
        // Read file to String
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/json/dayone_country.json")));

        // Cast it to class
        List<DayCountryEntity> rkiData = new ObjectMapper().readValue(json, new TypeReference<List<DayCountryEntity>>() {
        });

        // Assert
        Assertions.assertAll(
                () -> assertThat(rkiData.size()).isNotNull(),
                () -> assertThat(rkiData.get(0).getCountry()).isNotNull(),
                () -> assertThat(rkiData.get(0).getCountryCode()).isNotNull(),
                () -> assertThat(rkiData.get(0).getProvince()).isNotNull(),
                () -> assertThat(rkiData.get(0).getCity()).isNotNull(),
                () -> assertThat(rkiData.get(0).getCityCode()).isNotNull(),
                () -> assertThat(rkiData.get(0).getLat()).isNotNull(),
                () -> assertThat(rkiData.get(0).getLon()).isNotNull(),
                () -> assertThat(rkiData.get(0).getConfirmed()).isNotNull(),
                () -> assertThat(rkiData.get(0).getDeaths()).isNotNull(),
                () -> assertThat(rkiData.get(0).getRecovered()).isNotNull(),
                () -> assertThat(rkiData.get(0).getActive()).isNotNull(),
                () -> assertThat(rkiData.get(0).getDate()).isNotNull()
        );
        log.info(rkiData.get(0).toString());
    }

}
