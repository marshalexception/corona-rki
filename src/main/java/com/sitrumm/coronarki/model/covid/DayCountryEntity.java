package com.sitrumm.coronarki.model.covid;

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
public class DayCountryEntity implements Serializable {

    @JsonProperty("Country")
    String country;

    @JsonProperty("CountryCode")
    String countryCode;

    @JsonProperty("Province")
    String province;

    @JsonProperty("City")
    String city;

    @JsonProperty("CityCode")
    String cityCode;

    @JsonProperty("Lat")
    String lat;

    @JsonProperty("Lon")
    String lon;

    @JsonProperty("Confirmed")
    Long confirmed;

    @JsonProperty("Deaths")
    Long deaths;

    @JsonProperty("Recovered")
    Long recovered;

    @JsonProperty("Active")
    Long active;

    @JsonProperty("Date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate date;

}
