package com.english.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Word {
    private long id;
    @EqualsAndHashCode.Include
    @JsonProperty("en")
    private String en;
    @JsonProperty("ru")
    private String ru;
    @JsonProperty("tr")
    private String tr;
}
