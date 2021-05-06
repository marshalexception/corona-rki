package com.sitrumm.coronarki.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
@ToString
public class GlobalEntity implements Serializable {

    @JsonProperty("NewConfirmed")
    Long newConfirmed;

    @JsonProperty("TotalConfirmed")
    Long totalConfirmed;

    @JsonProperty("NewDeaths")
    Long newDeaths;

    @JsonProperty("TotalDeaths")
    Long totalDeaths;

    @JsonProperty("NewRecovered")
    Long newRecovered;

    @JsonProperty("TotalRecovered")
    Long totalRecovered;

    @JsonProperty("Date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate date;
}
