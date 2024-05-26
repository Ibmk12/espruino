package com.nihal.espruino.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
@Getter
@Setter
@ToString
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long measurementNumber;
    private float temperature;
    private float voltage;
    private float lightLevel;
    private int battery;

    @Embedded
    private Accel accel;

    @Embedded
    private MagData mag;

    private LocalDateTime timestamp;

}

