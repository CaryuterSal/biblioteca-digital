package com.edu.utez.bibliotecadigital.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "admin")
@ConfigurationPropertiesScan
public record AdminProperties(String username, String password) {
}
