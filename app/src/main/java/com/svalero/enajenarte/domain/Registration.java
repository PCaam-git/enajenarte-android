package com.svalero.enajenarte.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Registration {
    private long id;
    private LocalDate registrationDate; // format: date
    private boolean isPaid;
    private int numberOfTickets;
    private float amountPaid;
    private int rating;
    private long userId;
    private long workshopId;
}
