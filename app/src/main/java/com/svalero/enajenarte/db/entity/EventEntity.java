package com.svalero.enajenarte.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class EventEntity {

    @PrimaryKey
    private long id;

    private String title;
    private String location;
    private String eventDate;
    private float entryFee;
    private boolean isPublic;
    private long speakerId;

    public EventEntity() { }

    public EventEntity(long id, String title, String location, String eventDate,
                       float entryFee, boolean isPublic, long speakerId) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.eventDate = eventDate;
        this.entryFee = entryFee;
        this.isPublic = isPublic;
        this.speakerId = speakerId;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public float getEntryFee() { return entryFee; }
    public void setEntryFee(float entryFee) { this.entryFee = entryFee; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean aPublic) { isPublic = aPublic; }

    public long getSpeakerId() { return speakerId; }
    public void setSpeakerId(long speakerId) { this.speakerId = speakerId; }
}