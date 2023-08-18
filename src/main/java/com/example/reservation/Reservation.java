package com.example.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Třída reprezentující rezervace kurtů tenisového klubu
 */
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;
    @Column
    private String customerName;
    @Column
    private Integer customerPhone;
    @Column
    private Integer courtId;
    @Column
    private GameType gameType;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy", timezone = "UTC")
    @Column
    private LocalDate reservationDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm", timezone = "UTC")
    @Column
    private LocalTime fromTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm", timezone = "UTC")
    @Column
    private LocalTime toTime;
    @Column
    private Boolean deleted = false;
}
