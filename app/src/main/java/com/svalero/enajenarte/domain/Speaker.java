package com.svalero.enajenarte.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Speaker {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String speciality;
    private int yearsExperience;
}
