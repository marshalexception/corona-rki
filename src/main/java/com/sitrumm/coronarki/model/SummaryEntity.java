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
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate date;
}
