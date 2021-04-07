package com.sitrumm.coronarki.model.rki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

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
//    @JsonDeserialize(using = DateHandler.class)
    Date date;

}
