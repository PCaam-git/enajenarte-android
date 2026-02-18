package com.svalero.enajenarte.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private long id;
    private String title;
    private String location;
    private String eventDate;
    private float entryFee;
    private boolean isPublic;
    private long speakerId;
}
