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
public class WorkshopRequest {
    private String name;
    private String description;
    private LocalDate startDate;     // date
    private int durationMinutes;
    private float price;
    private int maxCapacity;         // solo InDto
    private boolean isOnline;
    private long speakerId;
}
