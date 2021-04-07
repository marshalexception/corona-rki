package com.sitrumm.coronarki.model.rki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.util.Date;

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
    Date date;

    @JsonProperty("Premium")
    PremiumEntity premium;

}
