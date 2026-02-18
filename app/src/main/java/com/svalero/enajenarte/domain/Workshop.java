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
public class Workshop {
    private long id;
    private String name;
    private String description;
    private LocalDate startDate;     // format: date
    private int durationMinutes;
    private float price;
    private boolean isOnline;
    private long speakerId;
}
