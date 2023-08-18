package com.example;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Třída reprezentující proměnné ze souboru application.properties
 */
@ConfigurationProperties(prefix = "data.initialization")
@Data
public class DataInitialization {
    private boolean app;
    private boolean test;
}
