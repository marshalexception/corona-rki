package com.sitrumm.coronarki.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
@ToString
public class PremiumEntity implements Serializable {

}
