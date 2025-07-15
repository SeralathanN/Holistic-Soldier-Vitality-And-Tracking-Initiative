package com.holisticsoldier.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "soldier_data")
public class SoldierData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;   // GPS Latitude
    private double longitude;  // GPS Longitude
    private double altitude;   // GPS Altitude in meters
    private double speed;      // GPS Speed in km/h

    private float atmosphericTemperature; // DHT11 Temperature in °C
    private float atmosphericHumidity;    // DHT11 Humidity in %
    private float bodyTemperature;        // DS18B20 Body Temperature in °C
    private int heartRate;                // HW-827 Analog Heart Rate sensor

    private LocalDateTime timestamp;      // Timestamp of the data

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
