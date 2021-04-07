package com.sitrumm.coronarki.model.rki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

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
    Date date;
}
