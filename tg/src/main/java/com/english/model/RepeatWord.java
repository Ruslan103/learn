package com.english.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RepeatWord {
    private Word word;
    private List<Word> responseOptions;
}
