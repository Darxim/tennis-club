package com.example.courtType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Třída reprezentující typ kurtu tenisového klubu
 */
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourtType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;
    @Column
    private String type;
    @Column
    private Integer price;
    @Column
    private Boolean deleted = false;
}
