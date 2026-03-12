package com.svalero.enajenarte.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workshops")
public class WorkshopEntity {
    @PrimaryKey
    private long id;

    private String name;
    private String description;

    // Room no soporta LocalDate, la fecha se guarda como String.
    private String startDate;
    private int durationMinutes;
    private float price;
    private int maxCapacity;
    private boolean isOnline;
    private String imageUri;
    private long speakerId;
    private Double latitude;
    private Double longitude;

    // Constructor vacío obligatorio para Room
    public WorkshopEntity() {}

    // Constructor completo SIN imageUri -> usado en WorkshopListModel
    public WorkshopEntity(long id, String name, String description, String startDate,
                          int durationMinutes, float price, int maxCapacity,
                          boolean isOnline, long speakerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.maxCapacity = maxCapacity;
        this.isOnline = isOnline;
        this.speakerId = speakerId;
    }

    // Constructor CON imageUri -> usado en WorkshopEditActivity
    public WorkshopEntity(long id, String name, String description, String startDate,
                          int durationMinutes, float price, int maxCapacity,
                          boolean isOnline, String imageUri,long speakerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.maxCapacity = maxCapacity;
        this.isOnline = isOnline;
        this.imageUri = imageUri;
        this.speakerId = speakerId;
    }



    // Getters y Setters (Room los necesita)
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }


    public long getSpeakerId() { return speakerId; }
    public void setSpeakerId(long speakerId) { this.speakerId = speakerId; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}

