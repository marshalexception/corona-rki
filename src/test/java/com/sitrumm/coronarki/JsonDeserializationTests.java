package com.sitrumm.coronarki;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitrumm.coronarki.model.DayCountryEntity;
import com.sitrumm.coronarki.model.SummaryEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class JsonDeserializationTests {

    @Test
    void testCovidSummaryEntity() throws IOException {
        // Read file to String
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/json/summary.json")));

        // Cast it to class
        SummaryEntity rkiData = new ObjectMapper().readValue(json, SummaryEntity.class);

        // Assert
        assertThat(rkiData).hasNoNullFieldsOrProperties();

        log.info(rkiData.toString());
    }

    @Test
    void testJsonToDayCountryEntity() throws IOException {
        // Read file to String
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/json/dayone_country.json")));

        // Cast it to class
        List<DayCountryEntity> rkiData = new ObjectMapper().readValue(json, new TypeReference<List<DayCountryEntity>>() {
        });

        // Assert
        Assertions.assertAll(
                () -> assertThat(rkiData).isNotEmpty(),
                () -> assertThat(rkiData.get(0)).hasNoNullFieldsOrProperties()
        );

        log.info(rkiData.get(0).toString());
    }

}
