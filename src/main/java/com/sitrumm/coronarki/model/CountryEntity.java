package com.sitrumm.coronarki.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
@ToString
public class CountryEntity {

    @JsonProperty("ID")
    String id;

    @JsonProperty("Country")
    String country;

    @JsonProperty("CountryCode")
    String countryCode;

    @JsonProperty("Slug")
    String slug;

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

    @JsonProperty("Premium")
    PremiumEntity premium;

}
