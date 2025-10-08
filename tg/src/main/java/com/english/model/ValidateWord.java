package com.english.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ValidateWord {
    private Word trueWord;
    private boolean isTrue;
}
