package com.sitrumm.coronarki.model.rki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
@ToString
public class SummaryEntity implements Serializable {

    @JsonProperty("ID")
    String id;

    @JsonProperty("Message")
    String message;

    @JsonProperty("Global")
    GlobalEntity global;

    @JsonProperty("Countries")
    List<CountryEntity> countries;

    @JsonProperty("Date")
    Date date;
}
