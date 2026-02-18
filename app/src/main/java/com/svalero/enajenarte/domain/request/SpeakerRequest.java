package com.svalero.enajenarte.domain.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeakerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String speciality;
    private int yearsExperience;
    private float workshopHoursTotal;
    private boolean available;
    private LocalDate joinDate; // date
}
