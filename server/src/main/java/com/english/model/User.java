package com.english.model;


import com.english.enums.Role;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class User {
    @EqualsAndHashCode.Include
    @NonNull
    private String login;
    private String name;
    private String surname;
    private Role role;
}
