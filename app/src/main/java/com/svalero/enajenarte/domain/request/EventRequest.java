package com.svalero.enajenarte.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    private String title;
    private String location;
    private String eventDate;
    private int expectedAttendance;
    private boolean isPublic;
    private float entryFee;
    private long speakerId;
}
