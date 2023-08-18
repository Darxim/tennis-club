package com.example.court;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Třída reprezentující kurt tenisového klubu
 */
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;
    @Column
    private Integer courtTypeId;
    @Column
    private Boolean deleted = false;
}
